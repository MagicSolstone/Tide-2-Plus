package net.minecraft.client.renderer.feature;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState$ShadowPiece;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record ShadowFeatureRenderer$Submit(Matrix4fc pose, float radius, List<EntityRenderState$ShadowPiece> pieces) implements SubmitNode {
   @Override
   public FeatureRendererType<ShadowFeatureRenderer$Submit> featureType() {
      return ShadowFeatureRenderer.TYPE;
   }
}
