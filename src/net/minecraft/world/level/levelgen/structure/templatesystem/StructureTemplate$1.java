package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

// $VF: synthetic class
class StructureTemplate$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.LEFT_RIGHT.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Mirror[Mirror.FRONT_BACK.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      $SwitchMap$net$minecraft$world$level$block$Rotation = new int[Rotation.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.COUNTERCLOCKWISE_90.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_90.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.CLOCKWISE_180.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$Rotation[Rotation.NONE.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
