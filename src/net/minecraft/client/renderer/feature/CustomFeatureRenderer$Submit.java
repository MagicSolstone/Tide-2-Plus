package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector$CustomGeometryRenderer;
import net.minecraft.client.renderer.feature.submit.BatchableSubmit;
import net.minecraft.client.renderer.rendertype.RenderType;

@Environment(EnvType.CLIENT)
public record CustomFeatureRenderer$Submit(PoseStack$Pose pose, RenderType renderType, SubmitNodeCollector$CustomGeometryRenderer customGeometryRenderer)
   implements BatchableSubmit {
   @Override
   public Object batchKey() {
      return this.renderType;
   }

   @Override
   public FeatureRendererType<CustomFeatureRenderer$Submit> featureType() {
      return CustomFeatureRenderer.TYPE;
   }
}
