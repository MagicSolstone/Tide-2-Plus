package com.mojang.renderpearl.backend.vulkan.init;

import com.mojang.renderpearl.api.device.BackendCreationException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.vulkan.VkPhysicalDevice;

@Environment(EnvType.CLIENT)
public interface FeatureSet$Condition {
   FeatureSet$Condition IDENTITY_CONDITION = var0 -> true;

   static FeatureSet$Condition reduce(final FeatureSet$Condition a, final FeatureSet$Condition b) {
      if (a == b) {
         return a;
      } else if (a == IDENTITY_CONDITION) {
         return b;
      } else {
         return b == IDENTITY_CONDITION ? a : device -> a.test(device) && b.test(device);
      }
   }

   boolean test(VkPhysicalDevice device) throws BackendCreationException;
}
