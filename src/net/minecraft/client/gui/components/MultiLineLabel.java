package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public interface MultiLineLabel {
   MultiLineLabel EMPTY = new MultiLineLabel$1();

   static MultiLineLabel create(final Font font, final Component... messages) {
      return create(font, Integer.MAX_VALUE, Integer.MAX_VALUE, messages);
   }

   static MultiLineLabel create(final Font font, final int maxWidth, final Component... messages) {
      return create(font, maxWidth, Integer.MAX_VALUE, messages);
   }

   static MultiLineLabel create(final Font font, final Component message, final int maxWidth) {
      return create(font, maxWidth, Integer.MAX_VALUE, message);
   }

   static MultiLineLabel create(final Font font, final int maxWidth, final int maxLines, final Component... messages) {
      return (MultiLineLabel)(messages.length == 0 ? EMPTY : new MultiLineLabel$2(messages, font, maxWidth, maxLines));
   }

   int visitLines(TextAlignment align, int anchorX, int topY, int lineHeight, ActiveTextCollector output);

   int getLineCount();

   int getWidth();
}
