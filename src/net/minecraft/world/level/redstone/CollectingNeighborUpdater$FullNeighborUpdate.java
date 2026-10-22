package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

record CollectingNeighborUpdater$FullNeighborUpdate(BlockState state, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
   implements CollectingNeighborUpdater$NeighborUpdates {
   @Override
   public boolean runNext(final Level level) {
      NeighborUpdater.executeUpdate(level, this.state, this.pos, this.block, this.orientation, this.movedByPiston);
      return false;
   }

   @Override
   public void forEachUpdatedPos(final Consumer<BlockPos> output) {
      output.accept(this.pos);
   }
}
