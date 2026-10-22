package net.minecraft.client.renderer;

import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record PostPass$InputTexture(String samplerName, GpuTextureView view, GpuSampler sampler) {
}
