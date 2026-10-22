package com.mojang.blaze3d.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.CardinalLighting$Type;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class Lighting$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$CardinalLighting$Type[CardinalLighting$Type.DEFAULT.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$CardinalLighting$Type[CardinalLighting$Type.NETHER.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
