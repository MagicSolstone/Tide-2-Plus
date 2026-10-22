package com.mojang.renderpearl.backend.opengl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record Uniform$Sampler(int samplerIndex) implements Uniform {
}
