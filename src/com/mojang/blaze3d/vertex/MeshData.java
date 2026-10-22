package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormatElement;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class MeshData implements AutoCloseable {
   private final ByteBufferBuilder$Result vertexBuffer;
   private ByteBufferBuilder$Result indexBuffer;
   private final MeshData$DrawState drawState;

   public MeshData(final ByteBufferBuilder$Result vertexBuffer, final MeshData$DrawState drawState) {
      this.vertexBuffer = vertexBuffer;
      this.drawState = drawState;
   }

   public static void decodeQuadCentroids(
      final ByteBuffer vertexBuffer, final int vertexCount, final VertexFormat format, final CompactVectorArray output, final int outputIndex
   ) {
      VertexFormatElement positionElement = format.getElement("Position");
      if (positionElement == null) {
         throw new IllegalArgumentException("Cannot identify quad centers with no position element");
      } else {
         int positionOffset = vertexBuffer.position() + positionElement.offset();
         int vertexStride = format.getVertexSize();
         int quadStride = vertexStride * 4;
         int quadCount = vertexCount / 4;

         for (int i = 0; i < quadCount; i++) {
            int firstPosOffset = i * quadStride + positionOffset;
            int secondPosOffset = firstPosOffset + vertexStride * 2;
            float x0 = vertexBuffer.getFloat(firstPosOffset + 0);
            float y0 = vertexBuffer.getFloat(firstPosOffset + 4);
            float z0 = vertexBuffer.getFloat(firstPosOffset + 8);
            float x1 = vertexBuffer.getFloat(secondPosOffset + 0);
            float y1 = vertexBuffer.getFloat(secondPosOffset + 4);
            float z1 = vertexBuffer.getFloat(secondPosOffset + 8);
            float xMid = (x0 + x1) / 2.0F;
            float yMid = (y0 + y1) / 2.0F;
            float zMid = (z0 + z1) / 2.0F;
            output.set(outputIndex + i, xMid, yMid, zMid);
         }
      }
   }

   public ByteBuffer vertexBuffer() {
      return this.vertexBuffer.byteBuffer();
   }

   @Nullable
   public ByteBuffer indexBuffer() {
      return this.indexBuffer != null ? this.indexBuffer.byteBuffer() : null;
   }

   public ByteBufferBuilder$Result vertexBufferSlice() {
      return this.vertexBuffer;
   }

   public MeshData$DrawState drawState() {
      return this.drawState;
   }

   @Nullable
   public MeshData$SortState sortQuads(final ByteBufferBuilder indexBufferTarget, final VertexSorting sorting) {
      if (this.drawState.primitiveTopology() != PrimitiveTopology.QUADS) {
         return null;
      } else {
         CompactVectorArray centroids = new CompactVectorArray(this.drawState.vertexCount() / 4);
         decodeQuadCentroids(this.vertexBuffer.byteBuffer(), this.drawState.vertexCount(), this.drawState.format(), centroids, 0);
         MeshData$SortState sortState = new MeshData$SortState(centroids, this.drawState.indexType());
         this.indexBuffer = sortState.buildSortedIndexBuffer(indexBufferTarget, sorting);
         return sortState;
      }
   }

   @Override
   public void close() {
      this.vertexBuffer.close();
      if (this.indexBuffer != null) {
         this.indexBuffer.close();
      }
   }
}
