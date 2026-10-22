package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ConduitRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class ConduitSpecialRenderer implements NoDataSpecialModelRenderer {
   private final SpriteGetter sprites;
   private final ModelPart model;

   public ConduitSpecialRenderer(final SpriteGetter sprites, final ModelPart model) {
      this.sprites = sprites;
      this.model = model;
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
      submitNodeCollector.submitModelPart(
         this.model,
         poseStack,
         ConduitRenderer.SHELL_TEXTURE.renderType(RenderTypes::entitySolid),
         lightCoords,
         overlayCoords,
         this.sprites.get(ConduitRenderer.SHELL_TEXTURE),
         -1,
         outlineColor
      );
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      this.model.getExtentsForGui(poseStack, output);
   }
}
