package com.mojang.blaze3d.font;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface GlyphProvider extends AutoCloseable {
   float BASELINE = 7.0F;

   @Override
   default void close() {
   }

   @Nullable
   default UnbakedGlyph getGlyph(final int codepoint) {
      return null;
   }

   IntSet getSupportedGlyphs();
}
