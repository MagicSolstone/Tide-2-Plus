package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.HitResult$Type;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class KeyboardHandler$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$phys$HitResult$Type[HitResult$Type.BLOCK.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$phys$HitResult$Type[HitResult$Type.ENTITY.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
