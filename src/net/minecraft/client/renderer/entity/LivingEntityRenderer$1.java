package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.scores.Team$Visibility;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class LivingEntityRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$scores$Team$Visibility[Team$Visibility.ALWAYS.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$world$scores$Team$Visibility[Team$Visibility.NEVER.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$world$scores$Team$Visibility[Team$Visibility.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$scores$Team$Visibility[Team$Visibility.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      $SwitchMap$net$minecraft$core$Direction = new int[Direction.values().length];

      try {
         $SwitchMap$net$minecraft$core$Direction[Direction.SOUTH.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction[Direction.WEST.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction[Direction.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction[Direction.EAST.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
