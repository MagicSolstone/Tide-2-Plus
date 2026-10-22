package net.minecraft.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface BlockBehaviour$PostProcess {
   @Nullable
   BlockPos getPostProcessPos(BlockState state, BlockGetter level, BlockPos pos);
}
