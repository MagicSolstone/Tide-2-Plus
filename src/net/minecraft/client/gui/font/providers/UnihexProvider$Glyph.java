package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.UnbakedGlyph;
import com.mojang.blaze3d.font.UnbakedGlyph$Stitcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;

@Environment(EnvType.CLIENT)
record UnihexProvider$Glyph(UnihexProvider$LineData contents, int left, int right) implements UnbakedGlyph {
   public int width() {
      return this.right - this.left + 1;
   }

   @Override
   public GlyphInfo info() {
      return new UnihexProvider$Glyph$1(this);
   }

   @Override
   public BakedGlyph bake(final UnbakedGlyph$Stitcher stitcher) {
      return stitcher.stitch(this.info(), new UnihexProvider$Glyph$2(this));
   }
}
