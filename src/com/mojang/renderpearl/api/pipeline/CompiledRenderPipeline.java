package com.mojang.renderpearl.api.pipeline;

import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface CompiledRenderPipeline extends UncheckedAutoCloseable {
   boolean isClosed();
}
