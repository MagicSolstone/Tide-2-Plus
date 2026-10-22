package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class SpriteIconButton extends Button {
   private static final Identifier BUTTON_DISABLED_SPRITE = Identifier.withDefaultNamespace("widget/button_disabled");
   private static final Identifier LOADING_SPRITE = Identifier.withDefaultNamespace("friends/loading");
   private static final Tooltip LOADING_TOOLTIP = Tooltip.create(Component.translatable("gui.friends.button.loading"));
   private static final int LOADING_SPRITE_W = 5;
   private static final int LOADING_SPRITE_H = 2;
   protected final WidgetSprites sprite;
   protected final int spriteWidth;
   protected final int spriteHeight;
   protected final int spriteOffsetX;
   protected final int spriteOffsetY;
   private final boolean switchToLoadingAfterPress;
   @Nullable
   private final Tooltip defaultTooltip;
   private boolean loading;

   private SpriteIconButton(
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
      super(0, 0, width, height, message, onPress, narration == null ? DEFAULT_NARRATION : narration);
      this.defaultTooltip = tooltip != null ? Tooltip.create(tooltip) : null;
      this.setTooltip(this.defaultTooltip);
      this.spriteWidth = spriteWidth;
      this.spriteHeight = spriteHeight;
      this.spriteOffsetX = spriteOffsetX;
      this.spriteOffsetY = spriteOffsetY;
      this.sprite = sprite;
      this.switchToLoadingAfterPress = switchToLoadingAfterPress;
   }

   public void setLoading(final boolean loading) {
      this.setLoading(loading, LOADING_TOOLTIP);
   }

   public void setLoading(final boolean loading, final Tooltip loadingTooltip) {
      this.loading = loading;
      if (loading) {
         this.setTooltip(loadingTooltip);
      } else {
         this.setTooltip(this.defaultTooltip);
      }
   }

   @Override
   public boolean isActive() {
      return super.isActive() && !this.loading;
   }

   @Override
   public void onPress(final InputWithModifiers input) {
      if (this.switchToLoadingAfterPress) {
         this.setLoading(true);
      }

      super.onPress(input);
   }

   protected void extractSprite(final GuiGraphicsExtractor graphics, final int x, final int y) {
      graphics.blitSprite(
         RenderPipelines.GUI_TEXTURED, this.sprite.get(this.isActive(), this.isHoveredOrFocused()), x, y, this.spriteWidth, this.spriteHeight, this.alpha
      );
   }

   protected boolean extractLoadingStateIfLoading(final GuiGraphicsExtractor graphics) {
      if (!this.loading) {
         return false;
      } else {
         graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED, BUTTON_DISABLED_SPRITE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha)
         );
         graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            LOADING_SPRITE,
            this.getX() + (this.getWidth() - 5) / 2,
            this.getY() + (this.getHeight() - 2) / 2,
            5,
            2,
            ARGB.white(this.alpha)
         );
         return true;
      }
   }

   public static SpriteIconButton$Builder builder(final Component message, final Button$OnPress onPress, final boolean iconOnly) {
      return new SpriteIconButton$Builder(message, onPress, iconOnly);
   }
}
