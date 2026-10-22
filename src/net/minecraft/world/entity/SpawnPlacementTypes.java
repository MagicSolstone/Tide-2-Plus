package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;

public interface SpawnPlacementTypes {
   SpawnPlacementType NO_RESTRICTIONS = (level, blockPos, type) -> true;
   SpawnPlacementType IN_WATER = (level, blockPos, type) -> {
      if (type != null && level.getWorldBorder().isWithinBounds(blockPos)) {
         BlockPos above = blockPos.above();
         return level.getFluidState(blockPos).is(FluidTags.WATER) && !level.getBlockState(above).isRedstoneConductor(level, above);
      } else {
         return false;
      }
   };
   SpawnPlacementType IN_LAVA = (level, blockPos, type) -> type != null && level.getWorldBorder().isWithinBounds(blockPos)
      ? level.getFluidState(blockPos).is(FluidTags.LAVA)
      : false;
   SpawnPlacementType ON_GROUND = new SpawnPlacementTypes$1();
}
