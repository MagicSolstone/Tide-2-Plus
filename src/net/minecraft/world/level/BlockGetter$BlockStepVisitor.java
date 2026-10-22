package net.minecraft.world.level;

import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface BlockGetter$BlockStepVisitor {
   boolean visit(BlockPos pos, int iteration);
}
