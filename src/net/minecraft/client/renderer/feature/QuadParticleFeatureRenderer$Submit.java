package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;

@Environment(EnvType.CLIENT)
public record QuadParticleFeatureRenderer$Submit(QuadParticleRenderState particles, boolean translucent) implements SubmitNode {
   @Override
   public FeatureRendererType<QuadParticleFeatureRenderer$Submit> featureType() {
      return QuadParticleFeatureRenderer.TYPE;
   }
}
