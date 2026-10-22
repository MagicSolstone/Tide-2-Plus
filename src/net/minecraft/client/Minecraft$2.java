package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.HitResult$Type;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class Minecraft$2 {
   static {
      try {
         $SwitchMap$net$minecraft$world$phys$HitResult$Type[HitResult$Type.ENTITY.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$phys$HitResult$Type[HitResult$Type.BLOCK.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$phys$HitResult$Type[HitResult$Type.MISS.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
