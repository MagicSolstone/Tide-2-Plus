package com.mojang.renderpearl.backend.vulkan.checkpoints;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum CheckpointExtension$CheckpointType {
   BEGIN_RENDER_PASS,
   END_RENDER_PASS;
}
