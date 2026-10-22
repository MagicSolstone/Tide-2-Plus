package net.minecraft.world.entity;

import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

// $VF: synthetic class
class Entity$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$entity$MoveSimulationType[MoveSimulationType.SERVER_AND_CLIENT.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$MoveSimulationType[MoveSimulationType.AUTHORITATIVE_SIDE.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$MoveSimulationType[MoveSimulationType.AUTHORITATIVE_SIDE_AND_SERVER.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      $SwitchMap$net$minecraft$world$level$block$Mirror = new int[Mirror.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.FRONT_BACK.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.LEFT_RIGHT.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      $SwitchMap$net$minecraft$world$level$block$Rotation = new int[Rotation.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_180.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
