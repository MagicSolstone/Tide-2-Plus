package net.minecraft.world.level.material;

import net.minecraft.world.level.block.state.StateDefinition$Builder;

public class WaterFluid$Flowing extends WaterFluid {
   @Override
   protected void createFluidStateDefinition(final StateDefinition$Builder<Fluid, FluidState> builder) {
      super.createFluidStateDefinition(builder);
      builder.add(LEVEL);
   }

   @Override
   public int getAmount(final FluidState fluidState) {
      return fluidState.getValue(LEVEL);
   }

   @Override
   public boolean isSource(final FluidState fluidState) {
      return false;
   }
}
