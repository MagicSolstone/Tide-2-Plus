package com.mojang.renderpearl.backend.vulkan;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.vulkan.VkCommandBuffer;

@Environment(EnvType.CLIENT)
record VulkanQueue$Submission$SubmitStage(
   List<VulkanQueue$Submission$SemaphoreOp> waits, List<VkCommandBuffer> commandBuffers, List<VulkanQueue$Submission$SemaphoreOp> signals
) {
   public VulkanQueue$Submission$SubmitStage() {
      this(new ReferenceArrayList(), new ReferenceArrayList(), new ReferenceArrayList());
   }
}
