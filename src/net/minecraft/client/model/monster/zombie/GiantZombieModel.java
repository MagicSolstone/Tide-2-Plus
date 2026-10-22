package net.minecraft.client.model.monster.zombie;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

@Environment(EnvType.CLIENT)
public class GiantZombieModel extends HumanoidModel<ZombieRenderState> {
   public GiantZombieModel(final ModelPart root) {
      super(root);
   }

   protected void setupAttackAnimation(final ZombieRenderState state) {
      super.setupAttackAnimation(state);
      AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, state.isAggressive, state);
   }
}
