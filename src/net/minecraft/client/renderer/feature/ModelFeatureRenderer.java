package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;

@Environment(EnvType.CLIENT)
public class ModelFeatureRenderer extends RenderTypeFeatureRenderer<ModelFeatureRenderer$Submit<?>> {
   public static final FeatureRendererType<ModelFeatureRenderer$Submit<?>> TYPE = FeatureRendererType.create("Entity Model");
   private final PoseStack poseStack = new PoseStack();

   @Override
   protected void buildGroup(final FeatureFrameContext context, final List<ModelFeatureRenderer$Submit<?>> submits) {
      for (ModelFeatureRenderer$Submit<?> submit : submits) {
         this.prepareModel(submit);
      }
   }

   private <S> void prepareModel(final ModelFeatureRenderer$Submit<S> submit) {
      this.poseStack.last().set(submit.pose());
      VertexConsumer buffer = this.getVertexBuilder(submit.renderType());
      if (submit.sheetedDecalPose() != null) {
         buffer = new SheetedDecalTextureGenerator(buffer, submit.sheetedDecalPose(), 1.0F);
      } else if (submit.uvMapping() != null) {
         buffer = submit.uvMapping().wrap(buffer);
      }

      Model<? super S> model = submit.model();
      model.setupAnim(submit.state());
      model.renderToBuffer(this.poseStack, buffer, submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
   }
}
