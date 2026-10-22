package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

class SpawnerBlockEntity$1 extends BaseSpawner {
   SpawnerBlockEntity$1(final SpawnerBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void broadcastEvent(final Level level, final BlockPos pos, final int id) {
      level.blockEvent(pos, Blocks.SPAWNER, id, 0);
   }

   @Override
   public void setNextSpawnData(@Nullable final Level level, final BlockPos pos, final SpawnData nextSpawnData) {
      super.setNextSpawnData(level, pos, nextSpawnData);
      if (level != null) {
         BlockState state = level.getBlockState(pos);
         level.sendBlockUpdated(pos, state, state, 260);
      }
   }
}
