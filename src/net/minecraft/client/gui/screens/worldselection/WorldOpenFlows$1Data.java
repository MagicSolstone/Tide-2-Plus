package net.minecraft.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldOptions;

@Environment(EnvType.CLIENT)
record WorldOpenFlows$1Data(LevelSettings levelSettings, WorldOptions options, Registry<LevelStem> existingDimensions) {
}
