package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class VulkanTransientMemory$TransientGpuBuffer extends VulkanGpuBuffer {
   private boolean closed;
   private final long bufferSubmitIndex;

   public VulkanTransientMemory$TransientGpuBuffer(
      final VulkanTransientMemory this$0, @GpuBuffer$Usage final long vkBuffer, final int usage, final int size, final long bufferSubmitIndex
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(vkBuffer, usage, size);
      this.closed = false;
      this.bufferSubmitIndex = bufferSubmitIndex;
   }

   @Override
   public void destroy() {
   }

   @Override
   public GpuBufferSlice$MappedView map(final long offset, final long length, final boolean read, final boolean write) {
      throw new IllegalStateException("Cannot map transient buffer");
   }

   @Override
   public boolean isClosed() {
      if (this.closed) {
         return true;
      } else {
         this.closed = this.bufferSubmitIndex < this.this$0.submitIndex;
         return this.closed;
      }
   }

   @Override
   public void close() {
      this.closed = true;
   }

   @Override
   public GpuBufferSlice slice(final long offset, final long length) {
      throw new IllegalStateException("Cannot slice transient buffer");
   }

   @Override
   public GpuBufferSlice slice() {
      throw new IllegalStateException("Cannot slice transient buffer");
   }
}
