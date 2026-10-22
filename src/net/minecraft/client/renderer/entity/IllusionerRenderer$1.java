package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.IllusionerRenderState;

@Environment(EnvType.CLIENT)
class IllusionerRenderer$1 extends ItemInHandLayer<IllusionerRenderState, IllagerModel<IllusionerRenderState>> {
   IllusionerRenderer$1(final IllusionerRenderer this$0, final RenderLayerParent renderer) {
      Objects.requireNonNull(this$0);
      super(renderer);
   }

   public void submit(
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final IllusionerRenderState state,
      final float yRot,
      final float xRot
   ) {
      if (state.isCastingSpell || state.isAggressive) {
         super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
      }
   }
}
