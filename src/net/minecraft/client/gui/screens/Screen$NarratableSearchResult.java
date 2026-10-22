package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratableEntry$NarrationPriority;

@Environment(EnvType.CLIENT)
public record Screen$NarratableSearchResult(NarratableEntry entry, int index, NarratableEntry$NarrationPriority priority) {
}
