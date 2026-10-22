package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

enum SupportType$2 {
   private final VoxelShape CENTER_SUPPORT_SHAPE = Block.column(2.0, 0.0, 10.0);

   @Override
   public boolean isSupporting(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
      return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.CENTER_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
   }
}
