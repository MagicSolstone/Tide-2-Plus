package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class MultifaceSpreader$DefaultSpreaderConfig implements MultifaceSpreader$SpreadConfig {
   protected final MultifaceBlock block;

   public MultifaceSpreader$DefaultSpreaderConfig(final MultifaceBlock block) {
      this.block = block;
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(final BlockState oldState, final BlockGetter level, final BlockPos placementPos, final Direction placementDirection) {
      return this.block.getStateForPlacement(oldState, level, placementPos, placementDirection);
   }

   protected boolean stateCanBeReplaced(
      final BlockGetter level, final BlockPos sourcePos, final BlockPos placementPos, final Direction placementDirection, final BlockState existingState
   ) {
      return existingState.isAir() || existingState.is(this.block) || existingState.is(Blocks.WATER) && existingState.getFluidState().isSource();
   }

   @Override
   public boolean canSpreadInto(final BlockGetter level, final BlockPos sourcePos, final MultifaceSpreader$SpreadPos spreadPos) {
      BlockState existingState = level.getBlockState(spreadPos.pos());
      return this.stateCanBeReplaced(level, sourcePos, spreadPos.pos(), spreadPos.face(), existingState)
         && this.block.isValidStateForPlacement(level, existingState, spreadPos.pos(), spreadPos.face());
   }
}
