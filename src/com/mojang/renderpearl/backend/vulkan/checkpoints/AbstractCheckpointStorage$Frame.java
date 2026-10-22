package com.mojang.renderpearl.backend.vulkan.checkpoints;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record AbstractCheckpointStorage$Frame(List<AbstractCheckpointStorage$Checkpoint> checkpoints) {
}
