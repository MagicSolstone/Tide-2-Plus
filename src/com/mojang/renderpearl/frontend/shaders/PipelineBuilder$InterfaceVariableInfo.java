package com.mojang.renderpearl.frontend.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record PipelineBuilder$InterfaceVariableInfo(int baseType, int vectorLength, boolean flatInterpolated) {
}
