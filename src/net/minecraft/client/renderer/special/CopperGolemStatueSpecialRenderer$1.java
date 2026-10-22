package net.minecraft.client.renderer.special;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.CopperGolemStatueBlock$Pose;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class CopperGolemStatueSpecialRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$CopperGolemStatueBlock$Pose[CopperGolemStatueBlock$Pose.STANDING.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$CopperGolemStatueBlock$Pose[CopperGolemStatueBlock$Pose.SITTING.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$CopperGolemStatueBlock$Pose[CopperGolemStatueBlock$Pose.STAR.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$CopperGolemStatueBlock$Pose[CopperGolemStatueBlock$Pose.RUNNING.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
