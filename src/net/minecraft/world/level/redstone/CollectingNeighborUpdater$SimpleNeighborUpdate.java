package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

record CollectingNeighborUpdater$SimpleNeighborUpdate(BlockPos pos, Block block, @Nullable Orientation orientation)
   implements CollectingNeighborUpdater$NeighborUpdates {
   @Override
   public boolean runNext(final Level level) {
      BlockState state = level.getBlockState(this.pos);
      NeighborUpdater.executeUpdate(level, state, this.pos, this.block, this.orientation, false);
      return false;
   }

   @Override
   public void forEachUpdatedPos(final Consumer<BlockPos> output) {
      output.accept(this.pos);
   }
}
