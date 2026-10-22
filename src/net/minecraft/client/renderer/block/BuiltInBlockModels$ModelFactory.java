package net.minecraft.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel$Unbaked;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface BuiltInBlockModels$ModelFactory {
   BlockModel$Unbaked create(BlockColors colors, BlockState state);
}
