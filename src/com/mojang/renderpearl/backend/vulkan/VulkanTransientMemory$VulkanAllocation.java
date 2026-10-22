package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocator$Block;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record VulkanTransientMemory$VulkanAllocation(long vkBuffer, long vmaAllocation, long hostPtr, long size, boolean suboptimal)
   implements TransientBlockAllocator$Allocator$Block {
}
