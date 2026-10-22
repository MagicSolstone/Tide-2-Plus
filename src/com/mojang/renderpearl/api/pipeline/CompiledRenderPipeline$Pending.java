package com.mojang.renderpearl.api.pipeline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface CompiledRenderPipeline$Pending {
   CompiledRenderPipeline$Pending NULL = () -> null;

   @Nullable
   CompiledRenderPipeline finishCompile();
}
