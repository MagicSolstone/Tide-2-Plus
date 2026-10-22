package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.network.chat.FontDescription;

@Environment(EnvType.CLIENT)
public interface Font$Provider {
   GlyphSource glyphs(FontDescription font);

   EffectGlyph effect();
}
