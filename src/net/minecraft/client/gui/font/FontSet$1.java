package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.SpecialGlyphs;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class FontSet$1 implements BakedGlyph {
   @Override
   public GlyphInfo info() {
      return SpecialGlyphs.MISSING;
   }

   @Nullable
   @Override
   public TextRenderable$Styled createGlyph(
      final float x, final float y, final int color, final int shadowColor, final Style style, final float boldOffset, final float shadowOffset
   ) {
      return null;
   }
}
