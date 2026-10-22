package net.minecraft.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;

enum UpgradeData$BlockFixers$5 {
   private UpgradeData$BlockFixers$5(final Block... blocks) {
   }

   @Override
   public BlockState updateShape(
      final BlockState state, final Direction direction, final BlockState neighbour, final LevelAccessor level, final BlockPos pos, final BlockPos neighbourPos
   ) {
      if (state.getValue(StemBlock.AGE) == 7) {
         Block fruit = state.is(Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
         if (neighbour.is(fruit)) {
            return (state.is(Blocks.PUMPKIN_STEM) ? Blocks.ATTACHED_PUMPKIN_STEM : Blocks.ATTACHED_MELON_STEM)
               .defaultBlockState()
               .setValue(HorizontalDirectionalBlock.FACING, direction);
         }
      }

      return state;
   }
}
