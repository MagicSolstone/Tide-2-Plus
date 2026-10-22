package net.minecraft.world.level.block;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

class SculkBehaviour$1 implements SculkBehaviour {
   @Override
   public boolean attemptSpreadVein(
      final LevelAccessor level, final BlockPos pos, final BlockState state, @Nullable final Collection<Direction> facings, final boolean postProcess
   ) {
      if (facings == null) {
         return ((SculkVeinBlock)Blocks.SCULK_VEIN).getSameSpaceSpreader().spreadAll(level.getBlockState(pos), level, pos, postProcess) > 0L;
      } else if (!facings.isEmpty()) {
         return !state.isAir() && !state.getFluidState().is(Fluids.WATER) ? false : SculkVeinBlock.regrow(level, pos, state, facings);
      } else {
         return SculkBehaviour.super.attemptSpreadVein(level, pos, state, facings, postProcess);
      }
   }

   @Override
   public int attemptUseCharge(
      final SculkSpreader$ChargeCursor cursor,
      final LevelAccessor level,
      final BlockPos originPos,
      final RandomSource random,
      final SculkSpreader spreader,
      final boolean spreadVeins
   ) {
      return cursor.getDecayDelay() > 0 ? cursor.getCharge() : 0;
   }

   @Override
   public int updateDecayDelay(final int age) {
      return Math.max(age - 1, 0);
   }
}
