package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface MultifaceSpreader$SpreadPredicate {
   boolean test(final BlockGetter level, final BlockPos sourcePos, final MultifaceSpreader$SpreadPos spreadPos);
}
