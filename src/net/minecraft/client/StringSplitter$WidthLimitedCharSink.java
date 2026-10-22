package net.minecraft.client;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;

@Environment(EnvType.CLIENT)
class StringSplitter$WidthLimitedCharSink implements FormattedCharSink {
   private float maxWidth;
   private int position;

   public StringSplitter$WidthLimitedCharSink(final StringSplitter this$0, final float maxWidth) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.maxWidth = maxWidth;
   }

   @Override
   public boolean accept(final int position, final Style style, final int codepoint) {
      this.maxWidth = this.maxWidth - this.this$0.widthProvider.getWidth(codepoint, style);
      if (this.maxWidth >= 0.0F) {
         this.position = position + Character.charCount(codepoint);
         return true;
      } else {
         return false;
      }
   }

   public int getPosition() {
      return this.position;
   }

   public void resetPosition() {
      this.position = 0;
   }
}
