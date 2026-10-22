package net.minecraft.world.level.chunk;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

enum UpgradeData$BlockFixers$4 {
   private final ThreadLocal<List<ObjectSet<BlockPos>>> queue = ThreadLocal.withInitial(() -> Lists.newArrayListWithCapacity(7));

   private UpgradeData$BlockFixers$4(final boolean chunky, final Block... blocks) {
   }

   @Override
   public BlockState updateShape(
      final BlockState state, final Direction direction, final BlockState neighbour, final LevelAccessor level, final BlockPos pos, final BlockPos neighbourPos
   ) {
      BlockState newState = state.updateShape(level, level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
      if (state != newState) {
         int distance = newState.getValue(BlockStateProperties.DISTANCE);
         List<ObjectSet<BlockPos>> queue = this.queue.get();
         if (queue.isEmpty()) {
            for (int i = 0; i < 7; i++) {
               queue.add(new ObjectOpenHashSet());
            }
         }

         queue.get(distance).add(pos.immutable());
      }

      return state;
   }

   @Override
   public void processChunk(final LevelAccessor level) {
      BlockPos$MutableBlockPos neighborPos = new BlockPos$MutableBlockPos();
      List<ObjectSet<BlockPos>> queue = this.queue.get();

      for (int neighborDistance = 2; neighborDistance < queue.size(); neighborDistance++) {
         int currentDistance = neighborDistance - 1;
         ObjectSet<BlockPos> set = queue.get(currentDistance);
         ObjectSet<BlockPos> newSet = queue.get(neighborDistance);
         ObjectIterator var8 = set.iterator();

         while (var8.hasNext()) {
            BlockPos pos = (BlockPos)var8.next();
            BlockState state = level.getBlockState(pos);
            if (state.getValue(BlockStateProperties.DISTANCE) >= currentDistance) {
               level.setBlock(pos, state.setValue(BlockStateProperties.DISTANCE, currentDistance), 18);
               if (neighborDistance != 7) {
                  for (Direction direction : DIRECTIONS) {
                     neighborPos.setWithOffset(pos, direction);
                     BlockState neighbor = level.getBlockState(neighborPos);
                     if (neighbor.hasProperty(BlockStateProperties.DISTANCE) && state.getValue(BlockStateProperties.DISTANCE) > neighborDistance) {
                        newSet.add(neighborPos.immutable());
                     }
                  }
               }
            }
         }
      }

      queue.clear();
   }
}
