package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

class Aquifer$1 implements Aquifer {
   Aquifer$1(final Aquifer$FluidPicker val$fluidRule) {
      this.val$fluidRule = val$fluidRulex;
   }

   @Nullable
   @Override
   public BlockState computeSubstance(final int blockX, final int blockY, final int blockZ, final double density) {
      return density > 0.0 ? null : this.val$fluidRule.computeFluid(blockX, blockY, blockZ).at(blockY);
   }

   @Override
   public boolean shouldScheduleFluidUpdate() {
      return false;
   }
}
