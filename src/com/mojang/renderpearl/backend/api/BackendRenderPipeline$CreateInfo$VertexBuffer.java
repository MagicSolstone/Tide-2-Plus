package com.mojang.renderpearl.backend.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record BackendRenderPipeline$CreateInfo$VertexBuffer(int bufferSlot, int stride, int stepRate) {
}
