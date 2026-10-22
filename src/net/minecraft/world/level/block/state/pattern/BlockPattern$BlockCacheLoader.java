package net.minecraft.world.level.block.state.pattern;

import com.google.common.cache.CacheLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

class BlockPattern$BlockCacheLoader extends CacheLoader<BlockPos, BlockInWorld> {
   private final LevelReader level;
   private final boolean loadChunks;

   public BlockPattern$BlockCacheLoader(final LevelReader level, final boolean loadChunks) {
      this.level = level;
      this.loadChunks = loadChunks;
   }

   public BlockInWorld load(final BlockPos key) {
      return new BlockInWorld(this.level, key, this.loadChunks);
   }
}
