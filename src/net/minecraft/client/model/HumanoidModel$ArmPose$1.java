package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.effects.SpearAnimations;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
enum HumanoidModel$ArmPose$1 {
   private HumanoidModel$ArmPose$1(final boolean twoHanded, final boolean affectsOffhandPose) {
   }

   @Override
   public <S extends ArmedEntityRenderState> void animateUseItem(
      final S state, final PoseStack poseStack, final float ticksUsingItem, final HumanoidArm arm, final ItemStack actualItem
   ) {
      SpearAnimations.thirdPersonUseItem(state, poseStack, ticksUsingItem, arm, actualItem);
   }
}
