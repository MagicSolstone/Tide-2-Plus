package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class StagingBuffer$Uploader implements AutoCloseable {
   private final CommandEncoder encoder;

   public StagingBuffer$Uploader(final StagingBuffer this$0, final CommandEncoder encoder) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.encoder = encoder;
   }

   public void copyTo(final StagingBuffer$BufferHandle srcBuffer, final GpuBuffer dstBuffer, final long dstOffset) {
      srcBuffer.checkValidFor(this.this$0);
      this.this$0.copyTo(this.encoder, dstBuffer, dstOffset, srcBuffer.offset, srcBuffer.size);
   }

   @Override
   public void close() {
      this.this$0.tryClearAndRotate();
   }

   public void checkValidFor(final StagingBuffer stagingBuffer) {
      if (stagingBuffer != this.this$0) {
         throw new IllegalArgumentException("Uploader is not valid for " + stagingBuffer);
      }
   }
}
