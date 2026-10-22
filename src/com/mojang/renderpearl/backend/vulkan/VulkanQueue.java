package com.mojang.renderpearl.backend.vulkan;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK12;
import org.lwjgl.vulkan.VkQueue;

@Environment(EnvType.CLIENT)
public record VulkanQueue(VkQueue vkQueue, int queueFamilyIndex) {
   public VulkanQueue(final VulkanDevice device, final int queueFamilyIndex, final int queueIndex) {
      this(new VkQueue(fetchVkQueue(device, queueFamilyIndex, queueIndex), device.vkDevice()), queueFamilyIndex);
   }

   private static long fetchVkQueue(final VulkanDevice device, final int queueFamilyIndex, final int queueIndex) {
      MemoryStack stack = MemoryStack.stackPush();

      long var5;
      try {
         PointerBuffer queueHandlePtr = stack.callocPointer(1);
         VK12.vkGetDeviceQueue(device.vkDevice(), queueFamilyIndex, queueIndex, queueHandlePtr);
         var5 = queueHandlePtr.get(0);
      } catch (Throwable var8) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (stack != null) {
         stack.close();
      }

      return var5;
   }

   public VulkanQueue$Submission beginSubmit() {
      return new VulkanQueue$Submission(this);
   }

   public void waitIdle() {
      VK12.vkQueueWaitIdle(this.vkQueue);
   }
}
