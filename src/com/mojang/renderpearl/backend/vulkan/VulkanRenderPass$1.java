package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.pipeline.IndexType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class VulkanRenderPass$1 {
   static {
      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$IndexType[IndexType.SHORT.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$IndexType[IndexType.INT.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
