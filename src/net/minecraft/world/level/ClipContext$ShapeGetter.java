package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface ClipContext$ShapeGetter {
   VoxelShape get(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);
}
