package net.minecraft.core;

import net.minecraft.world.level.block.Rotation;

// $VF: synthetic class
class BlockPos$8 {
   static {
      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$core$Direction$Axis[Direction$Axis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      $SwitchMap$net$minecraft$world$level$block$Rotation = new int[Rotation.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_180.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.NONE.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
