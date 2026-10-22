package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class FocusableTextWidget$Builder {
   private final Component message;
   private final Font font;
   private final int padding;
   private int maxWidth = -1;
   private boolean alwaysShowBorder = true;
   private FocusableTextWidget$BackgroundFill backgroundFill = FocusableTextWidget$BackgroundFill.ALWAYS;

   private FocusableTextWidget$Builder(final Component message, final Font font) {
      this(message, font, 4);
   }

   private FocusableTextWidget$Builder(final Component message, final Font font, final int padding) {
      this.message = message;
      this.font = font;
      this.padding = padding;
   }

   public FocusableTextWidget$Builder maxWidth(final int maxWidth) {
      this.maxWidth = maxWidth;
      return this;
   }

   public FocusableTextWidget$Builder textWidth(final int textWidth) {
      this.maxWidth = textWidth + this.padding * 2;
      return this;
   }

   public FocusableTextWidget$Builder alwaysShowBorder(final boolean alwaysShowBorder) {
      this.alwaysShowBorder = alwaysShowBorder;
      return this;
   }

   public FocusableTextWidget$Builder backgroundFill(final FocusableTextWidget$BackgroundFill backgroundFill) {
      this.backgroundFill = backgroundFill;
      return this;
   }

   public FocusableTextWidget build() {
      return new FocusableTextWidget(this.message, this.font, this.padding, this.maxWidth, this.backgroundFill, this.alwaysShowBorder);
   }
}
