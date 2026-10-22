package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockEntityType$BlockEntitySupplier<T extends BlockEntity> {
   T create(BlockPos worldPosition, BlockState blockState);
}
