package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SpriteIconButton$CenteredIcon extends SpriteIconButton {
   protected SpriteIconButton$CenteredIcon(
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
         int x = this.spriteOffsetX + this.getX() + this.getWidth() / 2 - this.spriteWidth / 2;
         int y = this.spriteOffsetY + this.getY() + this.getHeight() / 2 - this.spriteHeight / 2;
         this.extractSprite(graphics, x, y);
      }
   }
}
