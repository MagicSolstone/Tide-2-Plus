package com.mojang.blaze3d.vertex;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class StagingBuffer$BufferHandle implements AutoCloseable {
   private final int offset;
   private final int size;
   private boolean closed;

   public StagingBuffer$BufferHandle(final StagingBuffer this$0, final int offset, final int size) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.offset = offset;
      this.size = size;
   }

   private void checkValidFor(final StagingBuffer stagingBuffer) {
      if (this.closed) {
         throw new IllegalStateException("Buffer has already been closed");
      } else if (stagingBuffer != this.this$0) {
         throw new IllegalArgumentException("Buffer is not valid for " + stagingBuffer);
      }
   }

   public long size() {
      return this.size;
   }

   @Override
   public void close() {
      if (!this.closed) {
         this.closed = true;
         this.this$0.usedBufferCount--;
      }
   }
}
