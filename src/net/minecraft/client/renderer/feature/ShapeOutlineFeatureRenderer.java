package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class ShapeOutlineFeatureRenderer extends RenderTypeFeatureRenderer<ShapeOutlineFeatureRenderer$Submit> {
   public static final FeatureRendererType<ShapeOutlineFeatureRenderer$Submit> TYPE = FeatureRendererType.create("Shape Outline");

   @Override
   protected void buildGroup(final FeatureFrameContext context, final List<ShapeOutlineFeatureRenderer$Submit> submits) {
      Vector3f normal = new Vector3f();

      for (ShapeOutlineFeatureRenderer$Submit submit : submits) {
         PoseStack$Pose pose = submit.pose();
         int color = submit.color();
         float width = submit.width();
         VertexConsumer builder = this.getVertexBuilder(submit.renderType());
         submit.shape().forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            normal.set((float)(x2 - x1), (float)(y2 - y1), (float)(z2 - z1)).normalize();
            builder.addVertex(pose, (float)x1, (float)y1, (float)z1).setColor(color).setNormal(pose, normal).setLineWidth(width);
            builder.addVertex(pose, (float)x2, (float)y2, (float)z2).setColor(color).setNormal(pose, normal).setLineWidth(width);
         });
      }
   }
}
