package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface Aquifer {
   static Aquifer createDisabled(final Aquifer$FluidPicker fluidRule) {
      return new Aquifer$1(fluidRule);
   }

   @Nullable
   BlockState computeSubstance(int blockX, int blockY, int blockZ, double density);

   boolean shouldScheduleFluidUpdate();
}
