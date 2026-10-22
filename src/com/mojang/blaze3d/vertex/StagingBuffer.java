package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public abstract class StagingBuffer implements AutoCloseable {
   private int nextWriteOffset;
   private int usedBufferCount;

   public static StagingBuffer create(final String name, final GpuDevice gpuDevice, final int bufferSize) {
      return (StagingBuffer)(gpuDevice.getDeviceInfo().hintsAndWorkarounds().writeToBufferIsSlow() && gpuDevice.getDeviceInfo().features().persistentMapping()
         ? new StagingBuffer$PersistentlyMapped(name, bufferSize)
         : new StagingBuffer$Cpu(bufferSize));
   }

   public StagingBuffer$BufferHandle tryAppend(final ByteBuffer buffer) {
      int writeOffset = this.nextWriteOffset;
      int bufferSize = buffer.remaining();
      ByteBuffer writeBuffer = this.getWriteBuffer();
      if (bufferSize > writeBuffer.capacity()) {
         throw new IllegalArgumentException("Cannot fit allocation of size " + bufferSize + " into staging buffer of size " + writeBuffer.capacity());
      } else if (bufferSize > writeBuffer.capacity() - writeOffset) {
         return null;
      } else {
         MemoryUtil.memCopy(buffer, writeBuffer.position(writeOffset));
         this.nextWriteOffset += bufferSize;
         this.usedBufferCount++;
         return new StagingBuffer$BufferHandle(this, writeOffset, bufferSize);
      }
   }

   protected abstract ByteBuffer getWriteBuffer();

   protected abstract void copyTo(final CommandEncoder encoder, final GpuBuffer dstBuffer, long dstOffset, long stagingBufferOffset, long copySize);

   protected void rotateBuffer() {
   }

   public StagingBuffer$Uploader startUploading(final CommandEncoder encoder) {
      return new StagingBuffer$Uploader(this, encoder);
   }

   private void tryClearAndRotate() {
      if (this.nextWriteOffset > 0 && this.usedBufferCount == 0) {
         this.rotateBuffer();
         this.nextWriteOffset = 0;
      }
   }

   @Override
   public abstract void close();
}
