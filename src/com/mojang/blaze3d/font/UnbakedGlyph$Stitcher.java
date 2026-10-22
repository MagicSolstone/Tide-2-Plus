package com.mojang.blaze3d.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;

@Environment(EnvType.CLIENT)
public interface UnbakedGlyph$Stitcher {
   BakedGlyph stitch(GlyphInfo info, GlyphBitmap glyphBitmap);

   BakedGlyph getMissing();
}
