package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocator$Block;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record VulkanTransientMemory$TransferPair(VulkanTransientMemory$VulkanAllocation cpu, VulkanTransientMemory$VulkanAllocation gpu)
   implements TransientBlockAllocator$Allocator$Block {
   @Override
   public boolean suboptimal() {
      return this.cpu.suboptimal() || this.gpu.suboptimal();
   }
}
