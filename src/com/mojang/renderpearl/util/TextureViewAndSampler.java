package com.mojang.renderpearl.util;

import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record TextureViewAndSampler(GpuTextureView view, GpuSampler sampler) {
}
