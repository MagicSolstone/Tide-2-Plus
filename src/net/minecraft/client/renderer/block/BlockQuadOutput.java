package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.QuadInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.geometry.BakedQuad;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface BlockQuadOutput {
   void put(float x, float y, float z, BakedQuad quad, QuadInstance instance);
}
