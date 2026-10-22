package com.mojang.renderpearl.backend.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface TransientBlockAllocator$Allocator$Block {
   boolean suboptimal();
}
