package net.minecraft.client.gui.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.network.chat.FontDescription;

@Environment(EnvType.CLIENT)
record FontManager$CachedFontProvider$CachedEntry(FontDescription description, GlyphSource source) {
}
