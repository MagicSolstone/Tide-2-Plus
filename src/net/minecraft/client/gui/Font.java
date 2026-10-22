package net.minecraft.client.gui;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringDecomposer;

@Environment(EnvType.CLIENT)
public class Font {
   private static final float EFFECT_DEPTH = 0.01F;
   private static final float OVER_EFFECT_DEPTH = 0.01F;
   private static final float UNDER_EFFECT_DEPTH = -0.01F;
   public static final float SHADOW_DEPTH = 0.03F;
   public final int lineHeight = 9;
   private final RandomSource random = RandomSource.create();
   private final Font$Provider provider;
   private final StringSplitter splitter;

   public Font(final Font$Provider provider) {
      this.provider = provider;
      this.splitter = new StringSplitter((codepoint, style) -> this.getGlyphSource(style.getFont()).getGlyph(codepoint).info().getAdvance(style.isBold()));
   }

   private GlyphSource getGlyphSource(final FontDescription fontLocation) {
      return this.provider.glyphs(fontLocation);
   }

   public String bidirectionalShaping(final String text) {
      try {
         Bidi bidi = new Bidi(new ArabicShaping(8).shape(text), 127);
         bidi.setReorderingMode(0);
         return bidi.writeReordered(2);
      } catch (ArabicShapingException var3) {
         return text;
      }
   }

   public Font$PreparedText prepare8xTextOutline(final FormattedCharSequence str, final float x, final float y, final int outlineColor) {
      Font$PreparedTextBuilder outlineOutput = new Font$PreparedTextBuilder(this, 0.0F, 0.0F, outlineColor, false, false);

      for (int xo = -1; xo <= 1; xo++) {
         for (int yo = -1; yo <= 1; yo++) {
            if (xo != 0 || yo != 0) {
               float[] startX = new float[]{x};
               int finalXo = xo;
               int finalYo = yo;
               str.accept((position, style, codepoint) -> {
                  boolean bold = style.isBold();
                  BakedGlyph glyph = this.getGlyph(codepoint, style);
                  outlineOutput.x = startX[0] + finalXo * glyph.info().getShadowOffset();
                  outlineOutput.y = y + finalYo * glyph.info().getShadowOffset();
                  startX[0] += glyph.info().getAdvance(bold);
                  return outlineOutput.accept(position, style.withColor(outlineColor), glyph);
               });
            }
         }
      }

      outlineOutput.discardEffects();
      return outlineOutput;
   }

   public TextRenderable prepareBackground(final float x0, final float y0, final float x1, final float y1, final int color) {
      return this.provider.effect().createEffect(x0, y0, x1, y1, -0.01F, color, 0, 0.0F);
   }

   private BakedGlyph getGlyph(final int codepoint, final Style style) {
      GlyphSource glyphSource = this.getGlyphSource(style.getFont());
      BakedGlyph glyph = glyphSource.getGlyph(codepoint);
      if (style.isObfuscated() && codepoint != 32) {
         int targetWidth = Mth.ceil(glyph.info().getAdvance(false));
         glyph = glyphSource.getRandomGlyph(this.random, targetWidth);
      }

      return glyph;
   }

   public Font$PreparedText prepareText(String text, final float x, final float y, final int originalColor, final boolean drawShadow, final int backgroundColor) {
      if (this.isBidirectional()) {
         text = this.bidirectionalShaping(text);
      }

      Font$PreparedTextBuilder output = new Font$PreparedTextBuilder(this, x, y, originalColor, backgroundColor, drawShadow, false);
      StringDecomposer.iterateFormatted(text, Style.EMPTY, output);
      return output;
   }

   public Font$PreparedText prepareText(
      final FormattedCharSequence text,
      final float x,
      final float y,
      final int originalColor,
      final boolean drawShadow,
      final boolean includeEmpty,
      final int backgroundColor
   ) {
      Font$PreparedTextBuilder builder = new Font$PreparedTextBuilder(this, x, y, originalColor, backgroundColor, drawShadow, includeEmpty);
      text.accept(builder);
      return builder;
   }

   public int width(final String str) {
      return Mth.ceil(this.splitter.stringWidth(str));
   }

   public int width(final FormattedText text) {
      return Mth.ceil(this.splitter.stringWidth(text));
   }

   public int width(final FormattedCharSequence text) {
      return Mth.ceil(this.splitter.stringWidth(text));
   }

   public String plainSubstrByWidth(final String str, final int width, final boolean reverse) {
      return reverse ? this.splitter.plainTailByWidth(str, width, Style.EMPTY) : this.splitter.plainHeadByWidth(str, width, Style.EMPTY);
   }

   public String plainSubstrByWidth(final String str, final int width) {
      return this.splitter.plainHeadByWidth(str, width, Style.EMPTY);
   }

   public FormattedText substrByWidth(final FormattedText text, final int width) {
      return this.splitter.headByWidth(text, width, Style.EMPTY);
   }

   public int wordWrapHeight(final FormattedText input, final int textWidth) {
      return 9 * this.splitter.splitLines(input, textWidth, Style.EMPTY).size();
   }

   public List<FormattedCharSequence> split(final FormattedText input, final int maxWidth) {
      return Language.getInstance().getVisualOrder(this.splitter.splitLines(input, maxWidth, Style.EMPTY));
   }

   public List<FormattedText> splitIgnoringLanguage(final FormattedText input, final int maxWidth) {
      return this.splitter.splitLines(input, maxWidth, Style.EMPTY);
   }

   public boolean isBidirectional() {
      return Language.getInstance().isDefaultRightToLeft();
   }

   public StringSplitter getSplitter() {
      return this.splitter;
   }
}
