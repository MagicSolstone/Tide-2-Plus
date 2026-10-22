package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public enum SupportType {
   FULL,
   CENTER,
   RIGID;

   public abstract boolean isSupporting(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction);
}
