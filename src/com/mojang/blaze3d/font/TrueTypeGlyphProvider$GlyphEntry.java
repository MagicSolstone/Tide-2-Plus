package com.mojang.blaze3d.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class TrueTypeGlyphProvider$GlyphEntry {
   private final int index;
   @Nullable
   private volatile UnbakedGlyph glyph;

   private TrueTypeGlyphProvider$GlyphEntry(final int index) {
      this.index = index;
   }
}
