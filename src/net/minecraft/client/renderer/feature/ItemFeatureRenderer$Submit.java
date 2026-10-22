package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import net.minecraft.client.renderer.item.ItemStackRenderState$FoilType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;

@Environment(EnvType.CLIENT)
public record ItemFeatureRenderer$Submit(
   PoseStack$Pose pose,
   ItemDisplayContext displayContext,
   int lightCoords,
   int overlayCoords,
   int outlineColor,
   int[] tintLayers,
   List<BakedQuad> quads,
   ItemStackRenderState$FoilType foilType
) implements TranslucentSubmit {
   @Override
   public float distanceToCameraSq() {
      return TranslucentSubmit.computeDistanceToCameraSq(this.pose.pose());
   }

   @Override
   public FeatureRendererType<ItemFeatureRenderer$Submit> featureType() {
      return ItemFeatureRenderer.TYPE;
   }
}
