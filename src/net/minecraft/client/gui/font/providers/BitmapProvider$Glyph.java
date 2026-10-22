package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.UnbakedGlyph;
import com.mojang.blaze3d.font.UnbakedGlyph$Stitcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;

@Environment(EnvType.CLIENT)
record BitmapProvider$Glyph(float scale, BitmapProvider$ImageDataHolder imageData, int offsetX, int offsetY, int width, int height, int advance, int ascent)
   implements UnbakedGlyph {
   @Override
   public GlyphInfo info() {
      return GlyphInfo.simple(this.advance);
   }

   @Override
   public BakedGlyph bake(final UnbakedGlyph$Stitcher stitcher) {
      return stitcher.stitch(this.info(), new BitmapProvider$Glyph$1(this));
   }
}
