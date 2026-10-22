package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.font.GlyphInfo;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.TextRenderable$Styled;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class EmptyGlyph$1 implements BakedGlyph {
   EmptyGlyph$1(final EmptyGlyph this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public GlyphInfo info() {
      return this.this$0.info;
   }

   @Nullable
   @Override
   public TextRenderable$Styled createGlyph(
      final float x, final float y, final int color, final int shadowColor, final Style style, final float boldOffset, final float shadowOffset
   ) {
      return null;
   }
}
