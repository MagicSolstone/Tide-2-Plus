package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public enum HumanoidModel$ArmPose {
   EMPTY(false, false),
   ITEM(false, false),
   BLOCK(false, false),
   BOW_AND_ARROW(true, true),
   THROW_TRIDENT(false, true),
   CROSSBOW_CHARGE(true, true),
   CROSSBOW_HOLD(true, true),
   SPYGLASS(false, false),
   TOOT_HORN(false, false),
   BRUSH(false, false),
   SPEAR(false, false);

   private final boolean twoHanded;
   private final boolean affectsOffhandPose;

   private HumanoidModel$ArmPose(final boolean twoHanded, final boolean affectsOffhandPose) {
      this.twoHanded = twoHanded;
      this.affectsOffhandPose = affectsOffhandPose;
   }

   public boolean isTwoHanded() {
      return this.twoHanded;
   }

   public boolean affectsOffhandPose() {
      return this.affectsOffhandPose;
   }

   public <S extends ArmedEntityRenderState> void animateUseItem(
      final S state, final PoseStack poseStack, final float ticksUsingItem, final HumanoidArm arm, final ItemStack actualItem
   ) {
   }
}
