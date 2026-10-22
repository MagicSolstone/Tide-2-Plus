package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jspecify.annotations.Nullable;

class SpawnPlacementTypes$1 implements SpawnPlacementType {
   @Override
   public boolean isSpawnPositionOk(final LevelReader level, final BlockPos blockPos, @Nullable final EntityType<?> type) {
      if (type != null && level.getWorldBorder().isWithinBounds(blockPos)) {
         BlockPos above = blockPos.above();
         BlockPos below = blockPos.below();
         BlockState belowState = level.getBlockState(below);
         return !belowState.isValidSpawn(level, below, type)
            ? false
            : this.isValidEmptySpawnBlock(level, blockPos, type) && this.isValidEmptySpawnBlock(level, above, type);
      } else {
         return false;
      }
   }

   private boolean isValidEmptySpawnBlock(final LevelReader level, final BlockPos blockPos, final EntityType<?> type) {
      BlockState blockState = level.getBlockState(blockPos);
      return NaturalSpawner.isValidEmptySpawnBlock(level, blockPos, blockState, blockState.getFluidState(), type);
   }

   @Override
   public BlockPos adjustSpawnPosition(final LevelReader level, final BlockPos candidate) {
      BlockPos below = candidate.below();
      return level.getBlockState(below).isPathfindable(PathComputationType.LAND) ? below : candidate;
   }
}
