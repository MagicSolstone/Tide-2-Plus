package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.component.FireworkExplosion$Shape;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class FireworkParticles$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$item$component$FireworkExplosion$Shape[FireworkExplosion$Shape.SMALL_BALL.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$component$FireworkExplosion$Shape[FireworkExplosion$Shape.LARGE_BALL.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$component$FireworkExplosion$Shape[FireworkExplosion$Shape.STAR.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$component$FireworkExplosion$Shape[FireworkExplosion$Shape.CREEPER.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$item$component$FireworkExplosion$Shape[FireworkExplosion$Shape.BURST.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
