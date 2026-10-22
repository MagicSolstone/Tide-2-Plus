package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.pathfinder.PathComputationType;

// $VF: synthetic class
class SlabBlock$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$pathfinder$PathComputationType[PathComputationType.LAND.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$pathfinder$PathComputationType[PathComputationType.WATER.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$pathfinder$PathComputationType[PathComputationType.AIR.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      $SwitchMap$net$minecraft$world$level$block$state$properties$SlabType = new int[SlabType.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$SlabType[SlabType.TOP.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$SlabType[SlabType.BOTTOM.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$SlabType[SlabType.DOUBLE.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
