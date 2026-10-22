package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record PlayerTabOverlay$ScoreDisplayEntry(Component name, int score, @Nullable Component formattedScore, int scoreWidth) {
}
