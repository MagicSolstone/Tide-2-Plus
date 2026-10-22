package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.FormattedCharSequence;

@Environment(EnvType.CLIENT)
enum TextAlignment$1 {
   @Override
   public int calculateLeft(final int anchor, final int width) {
      return anchor;
   }

   @Override
   public int calculateLeft(final int anchor, final Font font, final FormattedCharSequence text) {
      return anchor;
   }
}
