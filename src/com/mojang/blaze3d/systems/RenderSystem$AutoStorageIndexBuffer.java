package com.mojang.blaze3d.systems;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.pipeline.IndexType;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public final class RenderSystem$AutoStorageIndexBuffer implements AutoCloseable {
   private final int vertexStride;
   private final int indexStride;
   private final RenderSystem$AutoStorageIndexBuffer$IndexGenerator generator;
   @Nullable
   private GpuBuffer buffer;
   private IndexType type = IndexType.SHORT;
   private int indexCount;
   private int maxRequestedIndexCount;

   private RenderSystem$AutoStorageIndexBuffer(
      final int vertexStride, final int indexStride, final RenderSystem$AutoStorageIndexBuffer$IndexGenerator generator
   ) {
      this.vertexStride = vertexStride;
      this.indexStride = indexStride;
      this.generator = generator;
   }

   @Override
   public void close() {
      if (this.buffer != null) {
         this.buffer.close();
      }
   }

   public boolean hasStorage(final int indexCount) {
      return indexCount <= this.indexCount;
   }

   public void requestIndexCount(final int indexCount) {
      this.maxRequestedIndexCount = Math.max(this.maxRequestedIndexCount, indexCount);
   }

   public void resizeToRequestedIndexCount() {
      this.ensureStorage(this.maxRequestedIndexCount);
   }

   public GpuBuffer getBuffer(final int indexCount) {
      this.requestIndexCount(indexCount);
      this.ensureStorage(indexCount);
      return this.buffer;
   }

   public GpuBuffer getBuffer() {
      return this.buffer;
   }

   private void ensureStorage(int indexCount) {
      if (!this.hasStorage(indexCount)) {
         indexCount = Mth.roundToward(indexCount * 2, this.indexStride);
         RenderSystem.LOGGER.debug("Growing IndexBuffer: Old limit {}, new limit {}.", this.indexCount, indexCount);
         int primitiveCount = indexCount / this.indexStride;
         int vertexCount = primitiveCount * this.vertexStride;
         IndexType type = IndexType.least(vertexCount);
         int bufferSize = Mth.roundToward(indexCount * type.bytes, 4);
         ByteBuffer data = MemoryUtil.memAlloc(bufferSize);

         try {
            this.type = type;
            IntConsumer intConsumer = this.intConsumer(data);

            for (int ii = 0; ii < indexCount; ii += this.indexStride) {
               this.generator.accept(intConsumer, ii * this.vertexStride / this.indexStride);
            }

            data.flip();
            if (this.buffer != null) {
               this.buffer.close();
            }

            this.buffer = RenderSystem.getDevice().createBuffer(() -> "Auto Storage index buffer", 64, data);
         } finally {
            MemoryUtil.memFree(data);
         }

         this.indexCount = indexCount;
      }
   }

   private IntConsumer intConsumer(final ByteBuffer buffer) {
      switch (this.type) {
         case SHORT:
            return value -> buffer.putShort((short)value);
         case INT:
         default:
            return buffer::putInt;
      }
   }

   public IndexType type() {
      return this.type;
   }
}
