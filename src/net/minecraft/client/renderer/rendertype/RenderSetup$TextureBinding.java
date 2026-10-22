package net.minecraft.client.renderer.rendertype;

import com.mojang.renderpearl.api.textures.GpuSampler;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record RenderSetup$TextureBinding(Identifier location, Supplier<GpuSampler> sampler) {
}
