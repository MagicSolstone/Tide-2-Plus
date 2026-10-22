package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.UnbakedGlyph;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class AllMissingGlyphProvider implements GlyphProvider {
   private static final UnbakedGlyph MISSING_INSTANCE = new AllMissingGlyphProvider$1();

   @Nullable
   @Override
   public UnbakedGlyph getGlyph(final int codepoint) {
      return MISSING_INSTANCE;
   }

   @Override
   public IntSet getSupportedGlyphs() {
      return IntSets.EMPTY_SET;
   }
}
