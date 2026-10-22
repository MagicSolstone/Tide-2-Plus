package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.feature.submit.BatchableSubmit;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.UvMapping;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ModelFeatureRenderer$Submit<S>(
   RenderType renderType,
   PoseStack$Pose pose,
   Model<? super S> model,
   S state,
   int lightCoords,
   int overlayCoords,
   int tintedColor,
   @Nullable UvMapping uvMapping,
   @Nullable PoseStack$Pose sheetedDecalPose
) implements BatchableSubmit, TranslucentSubmit {
   @Override
   public Object batchKey() {
      return this.renderType;
   }

   @Override
   public float distanceToCameraSq() {
      return TranslucentSubmit.computeDistanceToCameraSq(this.pose.pose());
   }

   @Override
   public FeatureRendererType<ModelFeatureRenderer$Submit<S>> featureType() {
      return (FeatureRendererType<ModelFeatureRenderer$Submit<S>>)ModelFeatureRenderer.TYPE;
   }
}
