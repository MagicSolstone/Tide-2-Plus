package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SpriteIconButton$Builder {
   private final Component message;
   private final Button$OnPress onPress;
   private final boolean iconOnly;
   private int width = 150;
   private int height = 20;
   @Nullable
   private WidgetSprites sprite;
   private int spriteWidth;
   private int spriteHeight;
   private int spriteOffsetX;
   private int spriteOffsetY;
   @Nullable
   private Component tooltip;
   @Nullable
   private Button$CreateNarration narration;
   private boolean switchToLoadingAfterPress;

   private SpriteIconButton$Builder(final Component message, final Button$OnPress onPress, final boolean iconOnly) {
      this.message = message;
      this.onPress = onPress;
      this.iconOnly = iconOnly;
   }

   public SpriteIconButton$Builder width(final int width) {
      this.width = width;
      return this;
   }

   public SpriteIconButton$Builder size(final int width, final int height) {
      this.width = width;
      this.height = height;
      return this;
   }

   public SpriteIconButton$Builder sprite(final Identifier sprite, final int spriteWidth, final int spriteHeight) {
      this.sprite = new WidgetSprites(sprite);
      this.spriteWidth = spriteWidth;
      this.spriteHeight = spriteHeight;
      return this;
   }

   public SpriteIconButton$Builder sprite(final WidgetSprites sprite, final int spriteWidth, final int spriteHeight) {
      this.sprite = sprite;
      this.spriteWidth = spriteWidth;
      this.spriteHeight = spriteHeight;
      return this;
   }

   public SpriteIconButton$Builder spriteOffset(final int spriteOffsetX, final int spriteOffsetY) {
      this.spriteOffsetX = spriteOffsetX;
      this.spriteOffsetY = spriteOffsetY;
      return this;
   }

   public SpriteIconButton$Builder tooltip(final Component tooltip) {
      this.tooltip = tooltip;
      return this;
   }

   public SpriteIconButton$Builder withTootip() {
      this.tooltip = this.message;
      return this;
   }

   public SpriteIconButton$Builder narration(final Button$CreateNarration narration) {
      this.narration = narration;
      return this;
   }

   public SpriteIconButton$Builder switchToLoadingAfterPress() {
      this.switchToLoadingAfterPress = true;
      return this;
   }

   public SpriteIconButton build() {
      if (this.sprite == null) {
         throw new IllegalStateException("Sprite not set");
      } else {
         return (SpriteIconButton)(this.iconOnly
            ? new SpriteIconButton$CenteredIcon(
               this.width,
               this.height,
               this.message,
               this.spriteWidth,
               this.spriteHeight,
               this.spriteOffsetX,
               this.spriteOffsetY,
               this.sprite,
               this.onPress,
               this.tooltip,
               this.narration,
               this.switchToLoadingAfterPress
            )
            : new SpriteIconButton$TextAndIcon(
               this.width,
               this.height,
               this.message,
               this.spriteWidth,
               this.spriteHeight,
               this.spriteOffsetX,
               this.spriteOffsetY,
               this.sprite,
               this.onPress,
               this.tooltip,
               this.narration,
               this.switchToLoadingAfterPress
            ));
      }
   }
}
