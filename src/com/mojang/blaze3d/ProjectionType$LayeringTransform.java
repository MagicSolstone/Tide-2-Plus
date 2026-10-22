package com.mojang.blaze3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface ProjectionType$LayeringTransform {
   void apply(Matrix4f matrix, float bias);
}
