package net.minecraft.world.level.material;

public class LavaFluid$Source extends LavaFluid {
   @Override
   public int getAmount(final FluidState fluidState) {
      return 8;
   }

   @Override
   public boolean isSource(final FluidState fluidState) {
      return true;
   }
}
