package com.mojang.renderpearl.backend.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface BackendRenderPipeline$Pending {
   BackendRenderPipeline$Pending NULL = () -> null;

   @Nullable
   BackendRenderPipeline finishCompile();
}
