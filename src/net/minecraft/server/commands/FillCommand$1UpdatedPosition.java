package net.minecraft.server.commands;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

record FillCommand$1UpdatedPosition(BlockPos pos, BlockState oldState) {
}
