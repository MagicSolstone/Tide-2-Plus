package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.UnbakedGlyph;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.CodepointMap;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class BitmapProvider implements GlyphProvider {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final BitmapProvider$ImageDataHolder imageData;
   private final CodepointMap<BitmapProvider$Glyph> glyphs;

   private BitmapProvider(final BitmapProvider$ImageDataHolder imageData, final CodepointMap<BitmapProvider$Glyph> glyphs) {
      this.imageData = imageData;
      this.glyphs = glyphs;
   }

   @Override
   public void close() {
      this.imageData.close();
   }

   @Nullable
   @Override
   public UnbakedGlyph getGlyph(final int codepoint) {
      return this.glyphs.get(codepoint);
   }

   @Override
   public IntSet getSupportedGlyphs() {
      return IntSets.unmodifiable(this.glyphs.keySet());
   }
}
