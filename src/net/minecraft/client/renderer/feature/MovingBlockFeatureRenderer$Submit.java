package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record MovingBlockFeatureRenderer$Submit(Matrix4fc pose, MovingBlockRenderState movingBlockRenderState, int outlineColor, boolean forceTranslucent)
   implements TranslucentSubmit {
   @Override
   public float distanceToCameraSq() {
      return TranslucentSubmit.computeDistanceToCameraSq(this.pose, 0.5F, 0.5F, 0.5F);
   }

   @Override
   public FeatureRendererType<MovingBlockFeatureRenderer$Submit> featureType() {
      return MovingBlockFeatureRenderer.TYPE;
   }
}
