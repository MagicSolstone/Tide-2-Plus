package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class MultiLineEditBox$Builder {
   private int x;
   private int y;
   private Component placeholder = CommonComponents.EMPTY;
   private int textColor = -2039584;
   private boolean textShadow = true;
   private int cursorColor = -3092272;
   private boolean showBackground = true;
   private boolean showDecorations = true;

   public MultiLineEditBox$Builder setX(final int x) {
      this.x = x;
      return this;
   }

   public MultiLineEditBox$Builder setY(final int y) {
      this.y = y;
      return this;
   }

   public MultiLineEditBox$Builder setPlaceholder(final Component placeholder) {
      this.placeholder = placeholder;
      return this;
   }

   public MultiLineEditBox$Builder setTextColor(final int textColor) {
      this.textColor = textColor;
      return this;
   }

   public MultiLineEditBox$Builder setTextShadow(final boolean textShadow) {
      this.textShadow = textShadow;
      return this;
   }

   public MultiLineEditBox$Builder setCursorColor(final int cursorColor) {
      this.cursorColor = cursorColor;
      return this;
   }

   public MultiLineEditBox$Builder setShowBackground(final boolean showBackground) {
      this.showBackground = showBackground;
      return this;
   }

   public MultiLineEditBox$Builder setShowDecorations(final boolean showDecorations) {
      this.showDecorations = showDecorations;
      return this;
   }

   public MultiLineEditBox build(final Font font, final int width, final int height, final Component narration) {
      return new MultiLineEditBox(
         font,
         this.x,
         this.y,
         width,
         height,
         this.placeholder,
         narration,
         this.textColor,
         this.textShadow,
         this.cursorColor,
         this.showBackground,
         this.showDecorations
      );
   }
}
