package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class ShulkerBoxSpecialRenderer implements NoDataSpecialModelRenderer {
   private final ShulkerBoxRenderer shulkerBoxRenderer;
   private final float openness;
   private final SpriteId sprite;

   public ShulkerBoxSpecialRenderer(final ShulkerBoxRenderer shulkerBoxRenderer, final float openness, final SpriteId sprite) {
      this.shulkerBoxRenderer = shulkerBoxRenderer;
      this.openness = openness;
      this.sprite = sprite;
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
      this.shulkerBoxRenderer.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, this.openness, null, this.sprite, outlineColor);
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      this.shulkerBoxRenderer.getExtents(this.openness, output);
   }
}
