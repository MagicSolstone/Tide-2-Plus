package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.animal.fish.Salmon$Variant;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class SalmonRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$entity$animal$fish$Salmon$Variant[Salmon$Variant.SMALL.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$animal$fish$Salmon$Variant[Salmon$Variant.MEDIUM.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$animal$fish$Salmon$Variant[Salmon$Variant.LARGE.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
