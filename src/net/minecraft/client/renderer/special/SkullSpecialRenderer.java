package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.model.object.skull.SkullModelBase$State;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class SkullSpecialRenderer implements NoDataSpecialModelRenderer {
   private final SkullModelBase model;
   private final float animation;
   private final RenderType renderType;

   public SkullSpecialRenderer(final SkullModelBase model, final float animation, final RenderType renderType) {
      this.model = model;
      this.animation = animation;
      this.renderType = renderType;
   }

   @Override
   public void submit(
      final PoseStack poseStack,
      final SubmitNodeCollector submitNodeCollector,
      final int lightCoords,
      final int overlayCoords,
      final boolean hasFoil,
      final int outlineColor
   ) {
      SkullBlockRenderer.submitSkull(this.animation, poseStack, submitNodeCollector, lightCoords, this.model, this.renderType, outlineColor, null);
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      SkullModelBase$State modelState = new SkullModelBase$State();
      modelState.animationPos = this.animation;
      this.model.setupAnim(modelState);
      this.model.root().getExtentsForGui(poseStack, output);
   }
}
