package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record BlockModelFeatureRenderer$Submit(
   PoseStack$Pose pose,
   RenderType renderType,
   List<BlockStateModelPart> modelParts,
   int[] tintLayers,
   int lightCoords,
   int overlayCoords,
   int tintColor,
   @Nullable PoseStack$Pose sheetedDecalPose
) implements TranslucentSubmit {
   @Override
   public float distanceToCameraSq() {
      return TranslucentSubmit.computeDistanceToCameraSq(this.pose.pose(), 0.5F, 0.5F, 0.5F);
   }

   @Override
   public FeatureRendererType<BlockModelFeatureRenderer$Submit> featureType() {
      return BlockModelFeatureRenderer.TYPE;
   }
}
