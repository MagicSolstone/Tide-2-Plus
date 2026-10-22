package net.minecraft.client;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;

@Environment(EnvType.CLIENT)
class StringSplitter$LineBreakFinder implements FormattedCharSink {
   private final float maxWidth;
   private int lineBreak;
   private Style lineBreakStyle;
   private boolean hadNonZeroWidthChar;
   private float width;
   private int lastSpace;
   private Style lastSpaceStyle;
   private int nextChar;
   private int offset;

   public StringSplitter$LineBreakFinder(final StringSplitter this$0, final float maxWidth) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.lineBreak = -1;
      this.lineBreakStyle = Style.EMPTY;
      this.lastSpace = -1;
      this.lastSpaceStyle = Style.EMPTY;
      this.maxWidth = Math.max(maxWidth, 1.0F);
   }

   @Override
   public boolean accept(final int position, final Style style, final int codepoint) {
      int adjustedPosition = position + this.offset;
      switch (codepoint) {
         case 10:
            return this.finishIteration(adjustedPosition, style);
         case 32:
            this.lastSpace = adjustedPosition;
            this.lastSpaceStyle = style;
         default:
            float charWidth = this.this$0.widthProvider.getWidth(codepoint, style);
            this.width += charWidth;
            if (!this.hadNonZeroWidthChar || !(this.width > this.maxWidth)) {
               this.hadNonZeroWidthChar |= charWidth != 0.0F;
               this.nextChar = adjustedPosition + Character.charCount(codepoint);
               return true;
            } else {
               return this.lastSpace != -1 ? this.finishIteration(this.lastSpace, this.lastSpaceStyle) : this.finishIteration(adjustedPosition, style);
            }
      }
   }

   private boolean finishIteration(final int lineBreak, final Style style) {
      this.lineBreak = lineBreak;
      this.lineBreakStyle = style;
      return false;
   }

   private boolean lineBreakFound() {
      return this.lineBreak != -1;
   }

   public int getSplitPosition() {
      return this.lineBreakFound() ? this.lineBreak : this.nextChar;
   }

   public Style getSplitStyle() {
      return this.lineBreakStyle;
   }

   public void addToOffset(final int delta) {
      this.offset += delta;
   }
}
