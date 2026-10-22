package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;

@Environment(EnvType.CLIENT)
class VindicatorRenderer$1 extends ItemInHandLayer<IllagerRenderState, IllagerModel<IllagerRenderState>> {
   VindicatorRenderer$1(final VindicatorRenderer this$0, final RenderLayerParent renderer) {
      Objects.requireNonNull(this$0);
      super(renderer);
   }

   public void submit(
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final IllagerRenderState state,
      final float yRot,
      final float xRot
   ) {
      if (state.isAggressive) {
         super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
      }
   }
}
