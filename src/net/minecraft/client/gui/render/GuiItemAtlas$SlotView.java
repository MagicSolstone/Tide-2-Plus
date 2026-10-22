package net.minecraft.client.gui.render;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record GuiItemAtlas$SlotView(GpuTextureView textureView, float u0, float v0, float u1, float v1) {
}
