package com.mojang.renderpearl.api.pipeline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum UniformType {
   COMBINED_IMAGE_SAMPLER,
   UNIFORM_BUFFER,
   TEXEL_BUFFER;
}
