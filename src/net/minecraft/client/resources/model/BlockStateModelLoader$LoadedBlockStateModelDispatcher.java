package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;

@Environment(EnvType.CLIENT)
record BlockStateModelLoader$LoadedBlockStateModelDispatcher(String source, BlockStateModelDispatcher contents) {
}
