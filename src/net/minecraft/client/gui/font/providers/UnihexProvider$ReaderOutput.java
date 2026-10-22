package net.minecraft.client.gui.font.providers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface UnihexProvider$ReaderOutput {
   void accept(int codepoint, UnihexProvider$LineData glyph);
}
