package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

record PointedDripstoneBlock$FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {
}
