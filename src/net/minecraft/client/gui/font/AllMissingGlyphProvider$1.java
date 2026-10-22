package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.UnbakedGlyph;
import com.mojang.blaze3d.font.UnbakedGlyph$Stitcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.SpecialGlyphs;

@Environment(EnvType.CLIENT)
class AllMissingGlyphProvider$1 implements UnbakedGlyph {
   @Override
   public GlyphInfo info() {
      return SpecialGlyphs.MISSING;
   }

   @Override
   public BakedGlyph bake(final UnbakedGlyph$Stitcher stitcher) {
      return stitcher.getMissing();
   }
}
