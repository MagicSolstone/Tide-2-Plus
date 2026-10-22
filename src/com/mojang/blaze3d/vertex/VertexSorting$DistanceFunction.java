package com.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface VertexSorting$DistanceFunction {
   float apply(Vector3f value);
}
