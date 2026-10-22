package net.minecraft.client.model.monster.zombie;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class DrownedModel extends ZombieModel<ZombieRenderState> {
   public DrownedModel(final ModelPart root) {
      super(root);
   }

   public static LayerDefinition createBodyLayer(final CubeDeformation g) {
      MeshDefinition mesh = HumanoidModel.createMesh(g, 0.0F);
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), PartPose.offset(5.0F, 2.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, g), PartPose.offset(1.9F, 12.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   protected void setupAttackAnimation(final ZombieRenderState state) {
      super.setupAttackAnimation(state);
      AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, state.isAggressive, state);
   }

   protected void setupSwimAnimation(final ZombieRenderState state, final float animationPos, final float swimAmount) {
      this.rightArm.xRot = Mth.rotLerpRad(swimAmount, this.rightArm.xRot, (float) (-Math.PI * 4.0 / 5.0))
         + swimAmount * 0.35F * Mth.sin(0.1F * state.ageInTicks);
      this.leftArm.xRot = Mth.rotLerpRad(swimAmount, this.leftArm.xRot, (float) (-Math.PI * 4.0 / 5.0)) - swimAmount * 0.35F * Mth.sin(0.1F * state.ageInTicks);
      this.rightArm.zRot = Mth.rotLerpRad(swimAmount, this.rightArm.zRot, -0.15F);
      this.leftArm.zRot = Mth.rotLerpRad(swimAmount, this.leftArm.zRot, 0.15F);
      this.leftLeg.xRot = this.leftLeg.xRot - swimAmount * 0.55F * Mth.sin(0.1F * state.ageInTicks);
      this.rightLeg.xRot = this.rightLeg.xRot + swimAmount * 0.55F * Mth.sin(0.1F * state.ageInTicks);
      this.head.xRot = 0.0F;
   }
}
