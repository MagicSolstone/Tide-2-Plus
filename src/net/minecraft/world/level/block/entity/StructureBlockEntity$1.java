package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

// $VF: synthetic class
class StructureBlockEntity$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_180.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$net$minecraft$world$level$block$Mirror = new int[Mirror.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.LEFT_RIGHT.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.FRONT_BACK.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
