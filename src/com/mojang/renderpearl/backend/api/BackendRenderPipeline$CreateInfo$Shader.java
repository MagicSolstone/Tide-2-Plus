package com.mojang.renderpearl.backend.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record BackendRenderPipeline$CreateInfo$Shader(String name, String entryPoint, SpvModule module) {
}
