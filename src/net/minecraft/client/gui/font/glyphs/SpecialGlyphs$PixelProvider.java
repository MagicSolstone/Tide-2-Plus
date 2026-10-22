package net.minecraft.client.gui.font.glyphs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface SpecialGlyphs$PixelProvider {
   int getColor(int x, int y);
}
