package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface FluidRenderer$Output {
   VertexConsumer getBuilder(ChunkSectionLayer layer);
}
