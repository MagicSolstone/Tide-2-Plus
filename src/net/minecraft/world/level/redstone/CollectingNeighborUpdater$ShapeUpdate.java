package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block$UpdateFlags;
import net.minecraft.world.level.block.state.BlockState;

record CollectingNeighborUpdater$ShapeUpdate(
   Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, @Block$UpdateFlags int updateFlags, int updateLimit
) implements CollectingNeighborUpdater$NeighborUpdates {
   @Override
   public boolean runNext(final Level level) {
      NeighborUpdater.executeShapeUpdate(level, this.direction, this.pos, this.neighborPos, this.neighborState, this.updateFlags, this.updateLimit);
      return false;
   }

   @Override
   public void forEachUpdatedPos(final Consumer<BlockPos> output) {
      output.accept(this.pos);
   }
}
