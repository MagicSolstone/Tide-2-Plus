package com.mojang.renderpearl.backend.vulkan.checkpoints;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record AbstractCheckpointStorage$Checkpoint(int id, String label, CheckpointExtension$CheckpointType type) {
}
