package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.device.GpuDevice;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class UberGpuBuffer$UberGpuBufferHeap extends TlsfAllocator$Heap {
   private final GpuBuffer gpuBuffer;

   public UberGpuBuffer$UberGpuBufferHeap(final long size, final GpuDevice gpuDevice, @GpuBuffer$Usage final int usage, final String name) {
      super(size);
      this.gpuBuffer = gpuDevice.createBuffer(() -> name, usage | 8 | 16, size);
   }
}
