package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.textures.FilterMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class GlSampler$1 {
   static {
      try {
         $SwitchMap$com$mojang$renderpearl$api$textures$FilterMode[FilterMode.NEAREST.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$textures$FilterMode[FilterMode.LINEAR.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
