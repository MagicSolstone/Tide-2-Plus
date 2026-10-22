package net.minecraft.client.renderer.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public interface BlockModel {
   void update(BlockModelRenderState output, BlockState blockState, BlockDisplayContext displayContext, long seed);
}
