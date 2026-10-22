package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public record FlameFeatureRenderer$Submit(PoseStack$Pose pose, EntityRenderState entityRenderState, Quaternionf rotation) implements SubmitNode {
   @Override
   public FeatureRendererType<FlameFeatureRenderer$Submit> featureType() {
      return FlameFeatureRenderer.TYPE;
   }
}
