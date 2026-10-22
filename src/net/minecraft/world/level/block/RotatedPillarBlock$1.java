package net.minecraft.world.level.block;

import net.minecraft.core.Direction$Axis;

// $VF: synthetic class
class RotatedPillarBlock$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$net$minecraft$core$Direction$Axis = new int[Direction$Axis.values().length];

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.Z.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
