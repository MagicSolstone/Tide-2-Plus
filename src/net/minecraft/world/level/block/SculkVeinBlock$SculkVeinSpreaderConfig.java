package net.minecraft.world.level.block;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

class SculkVeinBlock$SculkVeinSpreaderConfig extends MultifaceSpreader$DefaultSpreaderConfig {
   private final MultifaceSpreader$SpreadType[] spreadTypes;

   public SculkVeinBlock$SculkVeinSpreaderConfig(final SculkVeinBlock this$0, final MultifaceSpreader$SpreadType... spreadTypes) {
      Objects.requireNonNull(this$0);
      super(this$0);
      this.spreadTypes = spreadTypes;
   }

   @Override
   public boolean stateCanBeReplaced(
      final BlockGetter level, final BlockPos sourcePos, final BlockPos placementPos, final Direction placementDirection, final BlockState existingState
   ) {
      BlockState againstState = level.getBlockState(placementPos.relative(placementDirection));
      if (!againstState.is(Blocks.SCULK) && !againstState.is(Blocks.SCULK_CATALYST) && !againstState.is(Blocks.MOVING_PISTON)) {
         if (sourcePos.distManhattan(placementPos) == 2) {
            BlockPos neighourPos = sourcePos.relative(placementDirection.getOpposite());
            if (level.getBlockState(neighourPos).isFaceSturdy(level, neighourPos, placementDirection)) {
               return false;
            }
         }

         FluidState fluidState = existingState.getFluidState();
         if (!fluidState.isEmpty() && !fluidState.is(Fluids.WATER)) {
            return false;
         } else {
            return existingState.is(BlockTags.FIRE)
               ? false
               : existingState.canBeReplaced() || super.stateCanBeReplaced(level, sourcePos, placementPos, placementDirection, existingState);
         }
      } else {
         return false;
      }
   }

   @Override
   public MultifaceSpreader$SpreadType[] getSpreadTypes() {
      return this.spreadTypes;
   }

   @Override
   public boolean isOtherBlockValidAsSource(final BlockState state) {
      return !state.is(Blocks.SCULK_VEIN);
   }
}
