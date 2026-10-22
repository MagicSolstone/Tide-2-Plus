package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.shapes.VoxelShape;

@Environment(EnvType.CLIENT)
public record ShapeOutlineFeatureRenderer$Submit(PoseStack$Pose pose, VoxelShape shape, RenderType renderType, int color, float width) implements SubmitNode {
   @Override
   public FeatureRendererType<ShapeOutlineFeatureRenderer$Submit> featureType() {
      return ShapeOutlineFeatureRenderer.TYPE;
   }
}
