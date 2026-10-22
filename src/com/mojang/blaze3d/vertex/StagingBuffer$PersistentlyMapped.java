package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MappableRingBuffer;

@Environment(EnvType.CLIENT)
class StagingBuffer$PersistentlyMapped extends StagingBuffer {
   private final MappableRingBuffer mappableRingBuffer;
   private GpuBufferSlice$MappedView currentMappedView;
   private GpuBuffer currentGPUBuffer;
   private ByteBuffer currentBuffer;

   private StagingBuffer$PersistentlyMapped(final String name, final int bufferSize) {
      this.mappableRingBuffer = new MappableRingBuffer(() -> name + " staging buffer", 18, bufferSize / 2);
      this.currentGPUBuffer = this.mappableRingBuffer.currentBuffer();
      this.currentMappedView = this.currentGPUBuffer.map(false, true);
      this.currentBuffer = this.currentMappedView.data();
   }

   @Override
   protected ByteBuffer getWriteBuffer() {
      return this.currentBuffer;
   }

   @Override
   protected void copyTo(final CommandEncoder encoder, final GpuBuffer dstBuffer, final long dstOffset, final long stagingBufferOffset, final long copySize) {
      encoder.copyToBuffer(this.currentGPUBuffer.slice(stagingBufferOffset, copySize), dstBuffer.slice(dstOffset, copySize));
   }

   @Override
   protected void rotateBuffer() {
      this.currentMappedView.close();
      this.mappableRingBuffer.rotate();
      this.currentGPUBuffer = this.mappableRingBuffer.currentBuffer();
      this.currentMappedView = this.currentGPUBuffer.map(false, true);
      this.currentBuffer = this.currentMappedView.data();
   }

   @Override
   public void close() {
      this.currentMappedView.close();
      this.mappableRingBuffer.close();
   }
}
