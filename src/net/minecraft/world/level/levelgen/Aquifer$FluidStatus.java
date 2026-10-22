package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record Aquifer$FluidStatus(int fluidLevel, BlockState fluidType) {
   public BlockState at(final int blockY) {
      return blockY < this.fluidLevel ? this.fluidType : Blocks.AIR.defaultBlockState();
   }
}
