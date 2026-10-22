package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.HumanoidArm;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ArmedEntityRenderState$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$entity$HumanoidArm[HumanoidArm.LEFT.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$entity$HumanoidArm[HumanoidArm.RIGHT.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
