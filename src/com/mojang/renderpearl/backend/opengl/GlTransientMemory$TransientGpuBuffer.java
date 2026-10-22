package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GlTransientMemory$TransientGpuBuffer extends GlBuffer {
   private boolean closed;
   private final long bufferSubmitIndex;

   protected GlTransientMemory$TransientGpuBuffer(final GlTransientMemory this$0, @GpuBuffer$Usage final int handle, final int usage, final long size) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(usage, size, handle, true);
      this.closed = false;
      this.bufferSubmitIndex = this$0x.encoder.currentSubmitIndex();
   }

   @Override
   public boolean isClosed() {
      if (this.closed) {
         return true;
      } else {
         this.closed = this.bufferSubmitIndex < this.this$0.encoder.currentSubmitIndex();
         return this.closed;
      }
   }

   @Override
   public void close() {
      this.closed = true;
   }

   @Override
   public GpuBufferSlice$MappedView map(final long offset, final long length, final boolean read, final boolean write) {
      throw new IllegalStateException("Cannot map Transient buffer");
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
