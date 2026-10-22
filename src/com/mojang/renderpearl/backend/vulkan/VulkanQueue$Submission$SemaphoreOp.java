package com.mojang.renderpearl.backend.vulkan;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record VulkanQueue$Submission$SemaphoreOp(long vkSemaphore, long value, long stageMask) {
}
