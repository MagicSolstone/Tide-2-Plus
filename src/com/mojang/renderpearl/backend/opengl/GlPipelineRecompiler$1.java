package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.pipeline.ShaderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class GlPipelineRecompiler$1 {
   static {
      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$ShaderType[ShaderType.VERTEX.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$ShaderType[ShaderType.FRAGMENT.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
