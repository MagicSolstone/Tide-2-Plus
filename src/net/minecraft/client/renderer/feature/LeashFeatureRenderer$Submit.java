package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState$LeashState;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public record LeashFeatureRenderer$Submit(Matrix4f pose, EntityRenderState$LeashState leashState) implements SubmitNode {
   @Override
   public FeatureRendererType<LeashFeatureRenderer$Submit> featureType() {
      return LeashFeatureRenderer.TYPE;
   }
}
