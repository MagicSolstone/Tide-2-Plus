package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
class StagingBuffer$Cpu extends StagingBuffer {
   private final ByteBuffer stagingBuffer;

   private StagingBuffer$Cpu(final int bufferSize) {
      this.stagingBuffer = MemoryUtil.memAlloc(bufferSize);
   }

   @Override
   protected ByteBuffer getWriteBuffer() {
      return this.stagingBuffer;
   }

   @Override
   protected void copyTo(final CommandEncoder encoder, final GpuBuffer dstBuffer, final long dstOffset, final long stagingBufferOffset, final long copySize) {
      encoder.writeToBuffer(dstBuffer.slice(dstOffset, copySize), this.stagingBuffer.slice((int)stagingBufferOffset, (int)copySize));
   }

   @Override
   public void close() {
      MemoryUtil.memFree(this.stagingBuffer);
   }
}
