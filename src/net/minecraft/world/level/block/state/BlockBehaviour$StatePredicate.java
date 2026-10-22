package net.minecraft.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface BlockBehaviour$StatePredicate {
   boolean test(BlockState state, BlockGetter level, BlockPos pos);
}
