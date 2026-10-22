package net.minecraft.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

enum UpgradeData$BlockFixers$3 {
   private UpgradeData$BlockFixers$3(final Block... blocks) {
   }

   @Override
   public BlockState updateShape(
      final BlockState state, final Direction direction, final BlockState neighbour, final LevelAccessor level, final BlockPos pos, final BlockPos neighbourPos
   ) {
      if (neighbour.is(state.getBlock())
         && direction.getAxis().isHorizontal()
         && state.getValue(ChestBlock.TYPE) == ChestType.SINGLE
         && neighbour.getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
         Direction facing = state.getValue(ChestBlock.FACING);
         if (direction.getAxis() != facing.getAxis() && facing == neighbour.getValue(ChestBlock.FACING)) {
            ChestType newType = direction == facing.getClockWise() ? ChestType.LEFT : ChestType.RIGHT;
            level.setBlock(neighbourPos, neighbour.setValue(ChestBlock.TYPE, newType.getOpposite()), 18);
            if (facing == Direction.NORTH || facing == Direction.EAST) {
               BlockEntity one = level.getBlockEntity(pos);
               BlockEntity two = level.getBlockEntity(neighbourPos);
               if (one instanceof ChestBlockEntity firstChestBlockEntity && two instanceof ChestBlockEntity secondChestBlockEntity) {
                  ChestBlockEntity.swapContents(firstChestBlockEntity, secondChestBlockEntity);
               }
            }

            return state.setValue(ChestBlock.TYPE, newType);
         }
      }

      return state;
   }
}
