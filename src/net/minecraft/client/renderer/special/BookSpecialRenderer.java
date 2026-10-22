package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.model.object.book.BookModel$State;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class BookSpecialRenderer implements NoDataSpecialModelRenderer {
   private final SpriteGetter sprites;
   private final BookModel model;
   private final BookModel$State state;

   public BookSpecialRenderer(final SpriteGetter sprites, final BookModel model, final BookModel$State state) {
      this.sprites = sprites;
      this.model = model;
      this.state = state;
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
      submitNodeCollector.submitModel(
         this.model, this.state, poseStack, lightCoords, overlayCoords, -1, EnchantTableRenderer.BOOK_TEXTURE, this.sprites, outlineColor
      );
   }

   @Override
   public void getExtents(final Consumer<Vector3fc> output) {
      PoseStack poseStack = new PoseStack();
      this.model.setupAnim(this.state);
      this.model.root().getExtentsForGui(poseStack, output);
   }
}
