package net.minecraft.core.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockTransformer$DropStrategy$ResourcePopper {
   BlockTransformer$DropStrategy$ResourcePopper FROM_FACE = Block::popResourceFromFace;
   BlockTransformer$DropStrategy$ResourcePopper FROM_MIDDLE = (level, pos, var2, stack) -> Block.popResource(level, pos, stack);

   void pop(final Level level, final BlockPos pos, final Direction direction, final ItemStack stack);
}
