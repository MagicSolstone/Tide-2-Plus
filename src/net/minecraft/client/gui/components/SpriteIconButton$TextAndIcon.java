package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor$HoveredTextEffects;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SpriteIconButton$TextAndIcon extends SpriteIconButton {
   protected SpriteIconButton$TextAndIcon(
      final int width,
      final int height,
      final Component message,
      final int spriteWidth,
      final int spriteHeight,
      final int spriteOffsetX,
      final int spriteOffsetY,
      final WidgetSprites sprite,
      final Button$OnPress onPress,
      @Nullable final Component tooltip,
      @Nullable final Button$CreateNarration narration,
      final boolean switchToLoadingAfterPress
   ) {
      super(width, height, message, spriteWidth, spriteHeight, spriteOffsetX, spriteOffsetY, sprite, onPress, tooltip, narration, switchToLoadingAfterPress);
   }

   @Override
   public void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      if (!this.extractLoadingStateIfLoading(graphics)) {
         this.extractDefaultSprite(graphics);
         int left = this.getX() + 2;
         int right = this.getX() + this.getWidth() - this.spriteWidth - 4;
         int centerX = this.getX() + this.getWidth() / 2;
         ActiveTextCollector output = graphics.textRendererForWidget(this, GuiGraphicsExtractor$HoveredTextEffects.NONE);
         output.acceptScrolling(this.getMessage(), centerX, left, right, this.getY(), this.getY() + this.getHeight());
         int x = this.spriteOffsetX + this.getX() + this.getWidth() - this.spriteWidth - 2;
         int y = this.spriteOffsetY + this.getY() + this.getHeight() / 2 - this.spriteHeight / 2;
         this.extractSprite(graphics, x, y);
      }
   }
}
