package com.mojang.renderpearl.backend.vulkan;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface Destroyable {
   void destroy();
}
