package net.minecraft.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface BlockBehaviour$OffsetFunction {
   Vec3 evaluate(BlockState state, BlockPos pos);
}
