package net.minecraft.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem$AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder$Result;
import com.mojang.blaze3d.vertex.CompactVectorArray;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData$SortState;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StagedVertexBuffer implements AutoCloseable {
   private final ByteBufferBuilder stagingBuffer;
   private final StagedVertexBuffer$GpuBufferPool stagingGpuBufferPool;
   private final List<StagedVertexBuffer$Draw> draws = new ArrayList<>();
   @Nullable
   private StagedVertexBuffer$Draw lastBuildingDraw;
   @Nullable
   private BufferBuilder lastVertexBuilder;
   private final StagedVertexBuffer$GpuBufferPool vertexBufferPool;
   private final StagedVertexBuffer$GpuBufferPool indexBufferPool;
   @Nullable
   private GpuBuffer currentVertexBuffer;
   @Nullable
   private GpuBuffer currentIndexBuffer;

   public StagedVertexBuffer(final Supplier<String> label, final int initialCapacity) {
      this.stagingBuffer = new ByteBufferBuilder(initialCapacity);
      this.stagingGpuBufferPool = new StagedVertexBuffer$GpuBufferPool(() -> label.get() + " - Staging", 22);
      this.vertexBufferPool = new StagedVertexBuffer$GpuBufferPool(() -> label.get() + " - Vertex", 40);
      this.indexBufferPool = new StagedVertexBuffer$GpuBufferPool(() -> label.get() + " - Index", 72);
   }

   public StagedVertexBuffer$Draw appendDraw(final VertexFormat format, final PrimitiveTopology primitiveTopology) {
      return this.appendDraw(format, primitiveTopology, null);
   }

   public StagedVertexBuffer$Draw appendDraw(final VertexFormat format, final PrimitiveTopology primitiveTopology, @Nullable final VertexSorting quadSorting) {
      if (this.currentVertexBuffer != null) {
         throw new IllegalStateException("Cannot append draw after upload");
      } else if (quadSorting != null && primitiveTopology != PrimitiveTopology.QUADS) {
         throw new IllegalArgumentException("Cannot sort draw with " + primitiveTopology);
      } else {
         StagedVertexBuffer$Draw draw = new StagedVertexBuffer$Draw(format, primitiveTopology, quadSorting);
         this.draws.add(draw);
         return draw;
      }
   }

   public VertexConsumer getVertexBuilder(final StagedVertexBuffer$Draw draw) {
      if (this.currentVertexBuffer != null) {
         throw new IllegalStateException("Cannot append draw after upload");
      } else if (this.lastBuildingDraw == draw) {
         return Objects.requireNonNull(this.lastVertexBuilder);
      } else {
         this.finishLastVertexBuilder();
         this.lastBuildingDraw = draw;
         this.lastVertexBuilder = new BufferBuilder(this.stagingBuffer, draw.primitiveTopology, draw.format);
         return this.lastVertexBuilder;
      }
   }

   private void finishLastVertexBuilder() {
      if (this.lastVertexBuilder != null) {
         MeshData mesh = this.lastVertexBuilder.build();
         if (mesh != null) {
            Objects.requireNonNull(this.lastBuildingDraw).append(mesh);
         }

         this.lastVertexBuilder = null;
         this.lastBuildingDraw = null;
      }
   }

   public void upload() {
      if (this.currentVertexBuffer != null) {
         throw new IllegalStateException("Already uploaded");
      } else if (!this.draws.isEmpty()) {
         this.finishLastVertexBuilder();
         int nextVertexOffset = 0;
         int nextIndexOffset = 0;

         for (StagedVertexBuffer$Draw draw : this.draws) {
            if (!draw.isEmpty()) {
               draw.vertexOffset = Mth.roundToward(nextVertexOffset, draw.format.getVertexSize());
               nextVertexOffset = draw.vertexOffset + draw.vertexBufferSize;
               if (draw.quadSorting != null) {
                  IndexType indexType = draw.indexType();
                  draw.indexOffset = Mth.roundToward(nextIndexOffset, indexType.bytes);
                  nextIndexOffset = draw.indexOffset + draw.indexCount * indexType.bytes;
               } else {
                  RenderSystem$AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(draw.primitiveTopology);
                  autoIndices.getBuffer(draw.indexCount);
               }
            }
         }

         if (nextVertexOffset != 0) {
            GpuDevice device = RenderSystem.getDevice();
            this.currentVertexBuffer = this.vertexBufferPool.acquire(device, nextVertexOffset);
            this.currentIndexBuffer = nextIndexOffset > 0 ? this.indexBufferPool.acquire(device, nextIndexOffset) : null;
            this.uploadDrawsToBuffers(device, this.draws, this.currentVertexBuffer, this.currentIndexBuffer, nextVertexOffset, nextIndexOffset);
         }
      }
   }

   private void uploadDrawsToBuffers(
      final GpuDevice device,
      final List<StagedVertexBuffer$Draw> draws,
      final GpuBuffer vertexGpuBuffer,
      @Nullable final GpuBuffer indexGpuBuffer,
      final int vertexBufferSize,
      final int indexBufferSize
   ) {
      CommandEncoder commandEncoder = device.createCommandEncoder();
      int stagingBufferSize = vertexBufferSize + indexBufferSize;
      GpuBuffer stagingBuffer = this.stagingGpuBufferPool.acquire(device, stagingBufferSize);

      try (GpuBufferSlice$MappedView view = stagingBuffer.slice(0L, stagingBufferSize).map(false, true)) {
         ByteBuffer buffer = view.data();

         for (StagedVertexBuffer$Draw draw : draws) {
            if (!draw.isEmpty()) {
               buffer.position(draw.vertexOffset);

               for (ByteBufferBuilder$Result slice : draw.vertexBufferSlices) {
                  buffer.put(slice.byteBuffer());
               }
            }
         }

         for (StagedVertexBuffer$Draw drawx : draws) {
            if (!drawx.isEmpty()) {
               if (indexGpuBuffer != null && drawx.quadSorting != null) {
                  MeshData$SortState sortState = new MeshData$SortState(decodeSortingPoints(drawx), drawx.indexType());
                  buffer.position(vertexBufferSize + drawx.indexOffset);
                  sortState.writeSortedIndexBuffer(buffer, drawx.quadSorting);
               }

               drawx.freeVertexData();
            }
         }
      }

      commandEncoder.copyToBuffer(stagingBuffer.slice(0L, vertexBufferSize), vertexGpuBuffer.slice(0L, vertexBufferSize));
      if (indexGpuBuffer != null) {
         commandEncoder.copyToBuffer(stagingBuffer.slice(vertexBufferSize, indexBufferSize), indexGpuBuffer.slice(0L, indexBufferSize));
      }
   }

   private static CompactVectorArray decodeSortingPoints(final StagedVertexBuffer$Draw draw) {
      VertexFormat format = draw.format;
      CompactVectorArray points = new CompactVectorArray(draw.vertexCount / 4);
      int offset = 0;

      for (ByteBufferBuilder$Result vertexBuffer : draw.vertexBufferSlices) {
         int vertexCount = vertexBuffer.size() / format.getVertexSize();
         MeshData.decodeQuadCentroids(vertexBuffer.byteBuffer(), vertexCount, format, points, offset);
         offset += vertexCount / 4;
      }

      return points;
   }

   @Nullable
   public StagedVertexBuffer$ExecuteInfo getExecuteInfo(final StagedVertexBuffer$Draw draw) {
      if (draw.isEmpty()) {
         return null;
      } else if (this.currentVertexBuffer == null) {
         throw new IllegalStateException("Cannot execute before upload");
      } else {
         int baseVertex = draw.vertexOffset / draw.format.getVertexSize();
         if (this.currentIndexBuffer != null && draw.quadSorting != null) {
            IndexType indexType = draw.indexType();
            int firstIndex = draw.indexOffset / indexType.bytes;
            return new StagedVertexBuffer$ExecuteInfo(
               this.currentVertexBuffer, this.currentIndexBuffer, indexType, baseVertex, firstIndex, draw.indexCount, draw.primitiveTopology
            );
         } else {
            RenderSystem$AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(draw.primitiveTopology);
            autoIndices.requestIndexCount(draw.indexCount);
            return new StagedVertexBuffer$ExecuteInfo(
               this.currentVertexBuffer, null, autoIndices.type(), baseVertex, 0, draw.indexCount, draw.primitiveTopology
            );
         }
      }
   }

   public void requestIndexCount(final StagedVertexBuffer$Draw draw) {
      if (this.currentIndexBuffer == null || draw.quadSorting == null) {
         RenderSystem$AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(draw.primitiveTopology);
         autoIndices.requestIndexCount(draw.indexCount);
      }
   }

   public void endDraw() {
      this.draws.clear();
      this.currentVertexBuffer = null;
      this.currentIndexBuffer = null;
   }

   public void endFrame() {
      this.endDraw();
      GpuDevice device = RenderSystem.getDevice();
      this.stagingGpuBufferPool.endFrame(device);
      this.vertexBufferPool.endFrame(device);
      this.indexBufferPool.endFrame(device);
   }

   @Override
   public void close() {
      this.stagingBuffer.close();
      this.stagingGpuBufferPool.close();
      this.vertexBufferPool.close();
      this.indexBufferPool.close();
   }
}
