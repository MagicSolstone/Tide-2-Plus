package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class ImageWidget$Texture extends ImageWidget {
   private Identifier texture;
   private final int textureWidth;
   private final int textureHeight;

   public ImageWidget$Texture(
      final int x, final int y, final int width, final int height, final Identifier texture, final int textureWidth, final int textureHeight
   ) {
      super(x, y, width, height);
      this.texture = texture;
      this.textureWidth = textureWidth;
      this.textureHeight = textureHeight;
   }

   @Override
   protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.blit(
         RenderPipelines.GUI_TEXTURED,
         this.texture,
         this.getX(),
         this.getY(),
         0.0F,
         0.0F,
         this.getWidth(),
         this.getHeight(),
         this.textureWidth,
         this.textureHeight
      );
   }

   @Override
   public void updateResource(final Identifier identifier) {
      this.texture = identifier;
   }
}
