package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.properties.RedstoneSide;

// $VF: synthetic class
class RedstoneWireBlock$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.LEFT_RIGHT.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.FRONT_BACK.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      $SwitchMap$net$minecraft$world$level$block$Rotation = new int[Rotation.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_180.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      $SwitchMap$net$minecraft$world$level$block$state$properties$RedstoneSide = new int[RedstoneSide.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RedstoneSide[RedstoneSide.UP.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RedstoneSide[RedstoneSide.SIDE.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RedstoneSide[RedstoneSide.NONE.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
