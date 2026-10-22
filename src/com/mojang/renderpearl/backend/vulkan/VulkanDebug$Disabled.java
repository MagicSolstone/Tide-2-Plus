package com.mojang.renderpearl.backend.vulkan;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

@Environment(EnvType.CLIENT)
public class VulkanDebug$Disabled implements VulkanDebug {
   @Override
   public void chainCreateInfo(final VkInstanceCreateInfo instanceCreateInfo, final MemoryStack stack) {
   }

   @Override
   public void setup(final VkInstance vkInstance) {
   }

   @Override
   public void setObjectName(final VkDevice device, final int objectType, final long objectHandle, final String label) {
   }

   @Override
   public void setObjectName(final VkDevice device, final int objectType, final long objectHandle, final Supplier<String> label) {
   }

   @Override
   public void beginDebugGroup(final VkCommandBuffer buffer, final Supplier<String> label) {
   }

   @Override
   public void endDebugGroup(final VkCommandBuffer buffer) {
   }

   @Override
   public void destroy(final VkInstance instance) {
   }

   @Override
   public boolean enabled() {
      return false;
   }
}
