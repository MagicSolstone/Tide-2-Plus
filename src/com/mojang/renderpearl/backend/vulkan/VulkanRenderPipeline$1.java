package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.pipeline.UniformType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class VulkanRenderPipeline$1 {
   static {
      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$UniformType[UniformType.UNIFORM_BUFFER.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$UniformType[UniformType.COMBINED_IMAGE_SAMPLER.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$UniformType[UniformType.TEXEL_BUFFER.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
