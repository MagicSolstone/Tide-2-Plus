package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

final class CollectingNeighborUpdater$MultiNeighborUpdate implements CollectingNeighborUpdater$NeighborUpdates {
   private final BlockPos sourcePos;
   private final Block sourceBlock;
   @Nullable
   private Orientation orientation;
   @Nullable
   private final Direction skipDirection;
   private int idx = 0;

   public CollectingNeighborUpdater$MultiNeighborUpdate(
      final BlockPos sourcePos, final Block sourceBlock, @Nullable final Orientation orientation, @Nullable final Direction skipDirection
   ) {
      this.sourcePos = sourcePos;
      this.sourceBlock = sourceBlock;
      this.orientation = orientation;
      this.skipDirection = skipDirection;
      if (NeighborUpdater.UPDATE_ORDER[this.idx] == skipDirection) {
         this.idx++;
      }
   }

   @Override
   public boolean runNext(final Level level) {
      Direction direction = NeighborUpdater.UPDATE_ORDER[this.idx++];
      BlockPos neighborPos = this.sourcePos.relative(direction);
      BlockState state = level.getBlockState(neighborPos);
      Orientation orientation = null;
      if (level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
         if (this.orientation == null) {
            this.orientation = ExperimentalRedstoneUtils.initialOrientation(level, this.skipDirection == null ? null : this.skipDirection.getOpposite(), null);
         }

         orientation = this.orientation.withFront(direction);
      }

      NeighborUpdater.executeUpdate(level, state, neighborPos, this.sourceBlock, orientation, false);
      if (this.idx < NeighborUpdater.UPDATE_ORDER.length && NeighborUpdater.UPDATE_ORDER[this.idx] == this.skipDirection) {
         this.idx++;
      }

      return this.idx < NeighborUpdater.UPDATE_ORDER.length;
   }

   @Override
   public void forEachUpdatedPos(final Consumer<BlockPos> output) {
      for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
         if (direction != this.skipDirection) {
            BlockPos neighborPos = this.sourcePos.relative(direction);
            output.accept(neighborPos);
         }
      }
   }
}
