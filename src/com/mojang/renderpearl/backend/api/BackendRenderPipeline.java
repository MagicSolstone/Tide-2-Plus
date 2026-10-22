package com.mojang.renderpearl.backend.api;

import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface BackendRenderPipeline extends UncheckedAutoCloseable {
   boolean isClosed();
}
