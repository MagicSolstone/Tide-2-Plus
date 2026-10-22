package net.minecraft.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

enum UpgradeData$BlockFixers$1 {
   private UpgradeData$BlockFixers$1(final Block... blocks) {
   }

   @Override
   public BlockState updateShape(
      final BlockState state, final Direction direction, final BlockState neighbour, final LevelAccessor level, final BlockPos pos, final BlockPos neighbourPos
   ) {
      return state;
   }
}
