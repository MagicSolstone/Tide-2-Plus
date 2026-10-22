package com.mojang.renderpearl.backend.vulkan.checkpoints;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record CheckpointExtension$StageCheckpoint(long stage, CheckpointExtension$CheckpointType type, String label) {
}
