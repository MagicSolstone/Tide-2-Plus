package com.mojang.renderpearl.api.device;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record DeviceFeatures(
   boolean wireframeFillMode,
   boolean shaderDrawParameters,
   boolean multiDrawDirectInterleaved,
   boolean multiDrawDirectSeparate,
   boolean multiDrawIndirect,
   boolean drawIndirect,
   boolean nonZeroFirstInstance,
   boolean persistentMapping
) {
}
