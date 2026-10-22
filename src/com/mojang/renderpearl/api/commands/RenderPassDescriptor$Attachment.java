package com.mojang.renderpearl.api.commands;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record RenderPassDescriptor$Attachment<T>(GpuTextureView textureView, T clearValue) {
}
