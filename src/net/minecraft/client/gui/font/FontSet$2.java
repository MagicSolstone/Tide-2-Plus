package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphBitmap;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.UnbakedGlyph$Stitcher;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;

@Environment(EnvType.CLIENT)
class FontSet$2 implements UnbakedGlyph$Stitcher {
   FontSet$2(final FontSet this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public BakedGlyph stitch(final GlyphInfo glyphInfo, final GlyphBitmap glyphBitmap) {
      return Objects.requireNonNullElse(this.this$0.stitcher.stitch(glyphInfo, glyphBitmap), this.this$0.missingGlyph);
   }

   @Override
   public BakedGlyph getMissing() {
      return this.this$0.missingGlyph;
   }
}
