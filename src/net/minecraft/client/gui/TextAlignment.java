package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.FormattedCharSequence;

@Environment(EnvType.CLIENT)
public enum TextAlignment {
   LEFT,
   CENTER,
   RIGHT;

   public abstract int calculateLeft(int anchor, int width);

   public int calculateLeft(final int anchor, final Font font, final FormattedCharSequence text) {
      return this.calculateLeft(anchor, font.width(text));
   }
}
