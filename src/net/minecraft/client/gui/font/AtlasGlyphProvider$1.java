package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphInfo;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
class AtlasGlyphProvider$1 implements BakedGlyph {
   AtlasGlyphProvider$1(final AtlasGlyphProvider this$0, final TextureAtlasSprite val$sprite) {
      this.val$sprite = val$spritex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public GlyphInfo info() {
      return AtlasGlyphProvider.GLYPH_INFO;
   }

   @Override
   public TextRenderable$Styled createGlyph(
      final float x, final float y, final int color, final int shadowColor, final Style style, final float boldOffset, final float shadowOffset
   ) {
      return new AtlasGlyphProvider$Instance(
         this.this$0.renderTypes, this.this$0.atlas.getTextureView(), this.val$sprite, x, y, color, shadowColor, shadowOffset, style
      );
   }
}
