package com.mojang.renderpearl.backend.api;

import com.mojang.renderpearl.api.GpuFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record BackendRenderPipeline$CreateInfo$AttribBinding(int bufferSlot, int location, int offset, GpuFormat format) {
}
