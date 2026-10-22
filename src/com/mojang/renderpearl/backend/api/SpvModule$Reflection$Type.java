package com.mojang.renderpearl.backend.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SpvModule$Reflection$Type {
   int baseType();

   int dimensions();

   int vectorSize();

   int arrayDimensions();

   int arrayLength(int dimensionIndex);
}
