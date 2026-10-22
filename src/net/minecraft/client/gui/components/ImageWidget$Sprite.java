package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class ImageWidget$Sprite extends ImageWidget {
   private Identifier sprite;

   public ImageWidget$Sprite(final int x, final int y, final int width, final int height, final Identifier sprite) {
      super(x, y, width, height);
      this.sprite = sprite;
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
   }

   @Override
   public void updateResource(final Identifier identifier) {
      this.sprite = identifier;
   }
}
