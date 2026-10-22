package net.minecraft.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record FluidModel(
   ChunkSectionLayer layer,
   Material$Baked stillMaterial,
   Material$Baked flowingMaterial,
   @Nullable Material$Baked overlayMaterial,
   @Nullable BlockTintSource tintSource
) {
}
