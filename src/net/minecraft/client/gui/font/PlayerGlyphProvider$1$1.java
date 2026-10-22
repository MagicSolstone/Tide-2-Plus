package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphInfo;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
class PlayerGlyphProvider$1$1 implements BakedGlyph {
   PlayerGlyphProvider$1$1(final PlayerGlyphProvider$1 this$1, final Supplier val$skin, final boolean val$hat) {
      this.val$skin = val$skinx;
      this.val$hat = val$hatx;
      Objects.requireNonNull(this$1);
      super();
   }

   @Override
   public GlyphInfo info() {
      return PlayerGlyphProvider.GLYPH_INFO;
   }

   @Override
   public TextRenderable$Styled createGlyph(
      final float x, final float y, final int color, final int shadowColor, final Style style, final float boldOffset, final float shadowOffset
   ) {
      return new PlayerGlyphProvider$Instance(this.val$skin, this.val$hat, x, y, color, shadowColor, shadowOffset, style);
   }
}
