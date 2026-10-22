package net.minecraft.world.level.chunk;

import java.util.Objects;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

class LevelChunkSection$1BlockCounter implements PalettedContainer$CountConsumer<BlockState> {
   public int nonEmptyBlockCount;
   public int fluidCount;
   public int tickingBlockCount;
   public int tickingFluidCount;

   LevelChunkSection$1BlockCounter(final LevelChunkSection this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   public void accept(final BlockState state, final int count) {
      if (!state.isAir()) {
         this.nonEmptyBlockCount += count;
         if (state.isRandomlyTicking()) {
            this.tickingBlockCount += count;
         }

         FluidState fluid = state.getFluidState();
         if (!fluid.isEmpty()) {
            this.fluidCount += count;
            if (fluid.isRandomlyTicking()) {
               this.tickingFluidCount += count;
            }
         }
      }
   }
}
