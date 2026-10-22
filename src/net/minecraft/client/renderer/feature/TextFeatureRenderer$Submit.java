package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$DisplayMode;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record TextFeatureRenderer$Submit(Matrix4fc pose, Font$DisplayMode displayMode, int lightCoords, TextFeatureRenderer$Content content)
   implements TranslucentSubmit {
   @Override
   public float distanceToCameraSq() {
      return TranslucentSubmit.computeDistanceToCameraSq(this.pose);
   }

   @Override
   public FeatureRendererType<TextFeatureRenderer$Submit> featureType() {
      return TextFeatureRenderer.TYPE;
   }
}
