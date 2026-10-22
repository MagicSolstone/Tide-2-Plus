package com.mojang.blaze3d.systems;

import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class RenderSystem$1 {
   static {
      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$IndexType[IndexType.SHORT.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$IndexType[IndexType.INT.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology = new int[PrimitiveTopology.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.QUADS.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.LINES.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
