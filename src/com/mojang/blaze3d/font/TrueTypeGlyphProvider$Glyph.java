package com.mojang.blaze3d.font;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;

@Environment(EnvType.CLIENT)
class TrueTypeGlyphProvider$Glyph implements UnbakedGlyph {
   private final int width;
   private final int height;
   private final float bearingX;
   private final float bearingY;
   private final GlyphInfo info;
   private final int index;

   private TrueTypeGlyphProvider$Glyph(
      final TrueTypeGlyphProvider this$0, final float left, final float top, final int width, final int height, final float advance, final int index
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.width = width;
      this.height = height;
      this.info = GlyphInfo.simple(advance / this$0x.oversample);
      this.bearingX = left / this$0x.oversample;
      this.bearingY = top / this$0x.oversample;
      this.index = index;
   }

   @Override
   public GlyphInfo info() {
      return this.info;
   }

   @Override
   public BakedGlyph bake(final UnbakedGlyph$Stitcher stitcher) {
      return stitcher.stitch(this.info, new TrueTypeGlyphProvider$Glyph$1(this));
   }
}
