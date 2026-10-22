package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.TextAlignment;

@Environment(EnvType.CLIENT)
class MultiLineLabel$1 implements MultiLineLabel {
   @Override
   public int visitLines(final TextAlignment align, final int anchorX, final int topY, final int lineHeight, final ActiveTextCollector output) {
      return topY;
   }

   @Override
   public int getLineCount() {
      return 0;
   }

   @Override
   public int getWidth() {
      return 0;
   }
}
