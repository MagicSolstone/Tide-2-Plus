package net.minecraft.client.gui.narration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record ScreenNarrationCollector$EntryKey(NarratedElementType type, int depth) {
}
