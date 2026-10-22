package net.minecraft.world.level.block;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface SculkBehaviour {
   SculkBehaviour DEFAULT = new SculkBehaviour$1();

   default byte getSculkSpreadDelay() {
      return 1;
   }

   default void onDischarged(final LevelAccessor level, final BlockState state, final BlockPos pos, final RandomSource random) {
   }

   default boolean depositCharge(final LevelAccessor level, final BlockPos pos, final RandomSource random) {
      return false;
   }

   default boolean attemptSpreadVein(
      final LevelAccessor level, final BlockPos pos, final BlockState state, @Nullable final Collection<Direction> facings, final boolean postProcess
   ) {
      return ((MultifaceSpreadeableBlock)Blocks.SCULK_VEIN).getSpreader().spreadAll(state, level, pos, postProcess) > 0L;
   }

   default boolean canChangeBlockStateOnSpread() {
      return true;
   }

   default int updateDecayDelay(final int age) {
      return 1;
   }

   int attemptUseCharge(
      SculkSpreader$ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins
   );
}
