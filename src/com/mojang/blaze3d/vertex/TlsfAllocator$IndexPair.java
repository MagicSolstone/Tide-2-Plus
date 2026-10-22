package com.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record TlsfAllocator$IndexPair(int firstLevelIndex, int secondLevelIndex) {
}
