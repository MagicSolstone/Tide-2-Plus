package com.mojang.renderpearl.backend.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record TransientBlockAllocator$Allocation<T>(T block, long offset, long size) {
}
