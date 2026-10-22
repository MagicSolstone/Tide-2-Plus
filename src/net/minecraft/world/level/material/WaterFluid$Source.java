package net.minecraft.world.level.material;

public class WaterFluid$Source extends WaterFluid {
   @Override
   public int getAmount(final FluidState fluidState) {
      return 8;
   }

   @Override
   public boolean isSource(final FluidState fluidState) {
      return true;
   }
}
