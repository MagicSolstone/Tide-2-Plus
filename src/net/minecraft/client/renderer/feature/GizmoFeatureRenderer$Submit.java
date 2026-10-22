package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives$Group;
import net.minecraft.client.renderer.state.level.CameraRenderState;

@Environment(EnvType.CLIENT)
public record GizmoFeatureRenderer$Submit(DrawableGizmoPrimitives$Group group, CameraRenderState camera) implements SubmitNode {
   @Override
   public FeatureRendererType<GizmoFeatureRenderer$Submit> featureType() {
      return GizmoFeatureRenderer.TYPE;
   }
}
