package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record LevelLoadTracker$ClientLevelReady(long readyAt) implements LevelLoadTracker$ClientState {
}
