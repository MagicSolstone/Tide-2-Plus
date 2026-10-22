package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.font.GlyphInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.TextRenderable$Styled;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface BakedGlyph {
   GlyphInfo info();

   @Nullable
   TextRenderable$Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset);
}
