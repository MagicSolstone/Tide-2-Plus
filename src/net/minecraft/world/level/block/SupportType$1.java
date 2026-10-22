package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

enum SupportType$1 {
   @Override
   public boolean isSupporting(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
      return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction);
   }
}
