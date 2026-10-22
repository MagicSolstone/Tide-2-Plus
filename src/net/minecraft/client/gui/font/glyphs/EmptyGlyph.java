package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.UnbakedGlyph;
import com.mojang.blaze3d.font.UnbakedGlyph$Stitcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EmptyGlyph implements UnbakedGlyph {
   private final GlyphInfo info;

   public EmptyGlyph(final float advance) {
      this.info = GlyphInfo.simple(advance);
   }

   @Override
   public GlyphInfo info() {
      return this.info;
   }

   @Override
   public BakedGlyph bake(final UnbakedGlyph$Stitcher stitcher) {
      return new EmptyGlyph$1(this);
   }
}
