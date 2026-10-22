package net.minecraft.world.level.redstone;

import com.mojang.logging.LogUtils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Block$UpdateFlags;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class CollectingNeighborUpdater implements NeighborUpdater {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Level level;
   private final int maxChainedNeighborUpdates;
   private final ArrayDeque<CollectingNeighborUpdater$NeighborUpdates> stack = new ArrayDeque<>();
   private final List<CollectingNeighborUpdater$NeighborUpdates> addedThisLayer = new ArrayList<>();
   private int count = 0;
   @Nullable
   private Consumer<BlockPos> debugListener;

   public CollectingNeighborUpdater(final Level level, final int maxChainedNeighborUpdates) {
      this.level = level;
      this.maxChainedNeighborUpdates = maxChainedNeighborUpdates;
   }

   public void setDebugListener(@Nullable final Consumer<BlockPos> debugListener) {
      this.debugListener = debugListener;
   }

   @Override
   public void shapeUpdate(
      final Direction direction,
      final BlockState neighborState,
      final BlockPos pos,
      final BlockPos neighborPos,
      @Block$UpdateFlags final int updateFlags,
      final int updateLimit
   ) {
      this.addAndRun(
         pos, new CollectingNeighborUpdater$ShapeUpdate(direction, neighborState, pos.immutable(), neighborPos.immutable(), updateFlags, updateLimit)
      );
   }

   @Override
   public void neighborChanged(final BlockPos pos, final Block block, @Nullable final Orientation orientation) {
      this.addAndRun(pos, new CollectingNeighborUpdater$SimpleNeighborUpdate(pos, block, orientation));
   }

   @Override
   public void neighborChanged(
      final BlockState state, final BlockPos pos, final Block block, @Nullable final Orientation orientation, final boolean movedByPiston
   ) {
      this.addAndRun(pos, new CollectingNeighborUpdater$FullNeighborUpdate(state, pos.immutable(), block, orientation, movedByPiston));
   }

   @Override
   public void updateNeighborsAtExceptFromFacing(
      final BlockPos pos, final Block block, @Nullable final Direction skipDirection, @Nullable final Orientation orientation
   ) {
      this.addAndRun(pos, new CollectingNeighborUpdater$MultiNeighborUpdate(pos.immutable(), block, orientation, skipDirection));
   }

   private void addAndRun(final BlockPos pos, final CollectingNeighborUpdater$NeighborUpdates update) {
      boolean runningAlready = this.count > 0;
      boolean tooManyUpdates = this.maxChainedNeighborUpdates >= 0 && this.count >= this.maxChainedNeighborUpdates;
      this.count++;
      if (!tooManyUpdates) {
         if (runningAlready) {
            this.addedThisLayer.add(update);
         } else {
            this.stack.push(update);
         }
      } else if (this.count - 1 == this.maxChainedNeighborUpdates) {
         LOGGER.error("Too many chained neighbor updates. Skipping the rest. First skipped position: {}", pos.toShortString());
      }

      if (!runningAlready) {
         this.runUpdates();
      }
   }

   private void runUpdates() {
      try {
         while (!this.stack.isEmpty() || !this.addedThisLayer.isEmpty()) {
            for (int i = this.addedThisLayer.size() - 1; i >= 0; i--) {
               this.stack.push(this.addedThisLayer.get(i));
            }

            this.addedThisLayer.clear();
            CollectingNeighborUpdater$NeighborUpdates nextUpdates = this.stack.peek();
            if (this.debugListener != null) {
               nextUpdates.forEachUpdatedPos(this.debugListener);
            }

            while (this.addedThisLayer.isEmpty()) {
               if (!nextUpdates.runNext(this.level)) {
                  this.stack.pop();
                  break;
               }
            }
         }
      } finally {
         this.stack.clear();
         this.addedThisLayer.clear();
         this.count = 0;
      }
   }
}
