package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface MultifaceSpreader$SpreadConfig {
   @Nullable
   BlockState getStateForPlacement(final BlockState oldState, final BlockGetter level, final BlockPos placementPos, final Direction placementDirection);

   boolean canSpreadInto(final BlockGetter level, final BlockPos sourcePos, final MultifaceSpreader$SpreadPos spreadPos);

   default MultifaceSpreader$SpreadType[] getSpreadTypes() {
      return MultifaceSpreader.DEFAULT_SPREAD_ORDER;
   }

   default boolean hasFace(final BlockState state, final Direction face) {
      return MultifaceBlock.hasFace(state, face);
   }

   default boolean isOtherBlockValidAsSource(final BlockState state) {
      return false;
   }

   default boolean canSpreadFrom(final BlockState state, final Direction face) {
      return this.isOtherBlockValidAsSource(state) || this.hasFace(state, face);
   }

   default boolean placeBlock(final LevelAccessor level, final MultifaceSpreader$SpreadPos spreadPos, final BlockState oldState, final boolean postProcess) {
      BlockState spreadState = this.getStateForPlacement(oldState, level, spreadPos.pos(), spreadPos.face());
      if (spreadState != null) {
         if (postProcess) {
            level.getChunk(spreadPos.pos()).markPosForPostProcessing(spreadPos.pos());
         }

         return level.setBlock(spreadPos.pos(), spreadState, 2);
      } else {
         return false;
      }
   }
}
