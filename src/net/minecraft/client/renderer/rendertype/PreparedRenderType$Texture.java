package net.minecraft.client.renderer.rendertype;

import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record PreparedRenderType$Texture(String name, GpuTextureView textureView, GpuSampler sampler) {
}
