package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.backend.common.BaseGpuBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class VulkanGpuBuffer extends BaseGpuBuffer implements Destroyable {
   private final long vkBuffer;

   public VulkanGpuBuffer(final long vkBuffer, @GpuBuffer$Usage final int usage, final long size) {
      super(usage, size);
      this.vkBuffer = vkBuffer;
   }

   public long vkBuffer() {
      return this.vkBuffer;
   }
}
