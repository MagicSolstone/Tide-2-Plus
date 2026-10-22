package net.minecraft.client.gui.font;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class FontSet$Source implements GlyphSource {
   private final boolean filterFishyGlyphs;

   public FontSet$Source(final FontSet this$0, final boolean filterFishyGlyphs) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.filterFishyGlyphs = filterFishyGlyphs;
   }

   @Override
   public BakedGlyph getGlyph(final int codepoint) {
      return this.this$0.getGlyph(codepoint).select(this.filterFishyGlyphs).get();
   }

   @Override
   public BakedGlyph getRandomGlyph(final RandomSource random, final int width) {
      return this.this$0.getRandomGlyph(random, width);
   }
}
