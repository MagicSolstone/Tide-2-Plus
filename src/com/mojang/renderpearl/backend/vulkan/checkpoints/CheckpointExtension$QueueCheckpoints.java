package com.mojang.renderpearl.backend.vulkan.checkpoints;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record CheckpointExtension$QueueCheckpoints(long queue, List<CheckpointExtension$StageCheckpoint> checkpoints) {
}
