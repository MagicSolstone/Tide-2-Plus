package com.mojang.renderpearl.backend.vulkan;

import java.util.Set;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

@Environment(EnvType.CLIENT)
public interface VulkanDebug {
   static VulkanDebug create(final int verbosity, final boolean wantsDebugLabels, final Set<String> availableExtensions, final Set<String> enabledExtensions) {
      if ((verbosity > 0 || wantsDebugLabels) && availableExtensions.contains("VK_EXT_debug_utils")) {
         enabledExtensions.add("VK_EXT_debug_utils");
         return new VulkanDebug$Enabled(verbosity, wantsDebugLabels);
      } else {
         return new VulkanDebug$Disabled();
      }
   }

   void chainCreateInfo(VkInstanceCreateInfo instanceCreateInfo, MemoryStack stack);

   void setup(VkInstance vkInstance);

   void setObjectName(VkDevice device, int objectType, long objectHandle, String label);

   void setObjectName(VkDevice device, int objectType, long objectHandle, Supplier<String> label);

   void beginDebugGroup(VkCommandBuffer buffer, Supplier<String> label);

   void endDebugGroup(VkCommandBuffer buffer);

   void destroy(VkInstance instance);

   boolean enabled();
}
