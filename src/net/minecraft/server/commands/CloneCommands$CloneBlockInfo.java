package net.minecraft.server.commands;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

record CloneCommands$CloneBlockInfo(
   BlockPos pos, BlockState state, @Nullable CloneCommands$CloneBlockEntityInfo blockEntityInfo, BlockState previousStateAtDestination
) {
}
