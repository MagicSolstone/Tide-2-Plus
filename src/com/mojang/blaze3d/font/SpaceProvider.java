package com.mojang.blaze3d.font;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SpaceProvider implements GlyphProvider {
   private final Int2ObjectMap<EmptyGlyph> glyphs;

   public SpaceProvider(final Map<Integer, Float> advances) {
      this.glyphs = new Int2ObjectOpenHashMap(advances.size());
      advances.forEach((codepoint, advance) -> this.glyphs.put(codepoint, new EmptyGlyph(advance)));
   }

   @Nullable
   @Override
   public UnbakedGlyph getGlyph(final int codepoint) {
      return (UnbakedGlyph)this.glyphs.get(codepoint);
   }

   @Override
   public IntSet getSupportedGlyphs() {
      return IntSets.unmodifiable(this.glyphs.keySet());
   }
}
