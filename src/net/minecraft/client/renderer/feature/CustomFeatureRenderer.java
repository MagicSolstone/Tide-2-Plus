package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CustomFeatureRenderer extends RenderTypeFeatureRenderer<CustomFeatureRenderer$Submit> {
   public static final FeatureRendererType<CustomFeatureRenderer$Submit> TYPE = FeatureRendererType.create("Custom");

   @Override
   protected void buildGroup(final FeatureFrameContext context, final List<CustomFeatureRenderer$Submit> submits) {
      for (CustomFeatureRenderer$Submit submit : submits) {
         VertexConsumer builder = this.getVertexBuilder(submit.renderType());
         submit.customGeometryRenderer().render(submit.pose(), builder);
      }
   }
}
