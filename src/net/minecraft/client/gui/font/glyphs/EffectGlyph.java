package net.minecraft.client.gui.font.glyphs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.TextRenderable;

@Environment(EnvType.CLIENT)
public interface EffectGlyph {
   TextRenderable createEffect(float x0, float y0, float x1, float y1, float depth, int color, int shadowColor, float shadowOffset);
}
