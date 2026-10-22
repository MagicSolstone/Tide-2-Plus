package net.minecraft.world.level.blockscan;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.jspecify.annotations.Nullable;

public class FilteredSectionCache {
   private final LevelReader level;
   private final FilteredSectionCache$CacheEntry[] sectionCache;
   private final Predicate<BlockState> statePredicate;

   public FilteredSectionCache(final LevelReader level, final Predicate<BlockState> statePredicate) {
      this.level = level;
      this.statePredicate = statePredicate;
      this.sectionCache = new FilteredSectionCache$CacheEntry[8];
   }

   @Nullable
   public BlockState getBlockState(final BlockPos pos) {
      int sectionX = SectionPos.blockToSectionCoord(pos.getX());
      int sectionY = SectionPos.blockToSectionCoord(pos.getY());
      int sectionZ = SectionPos.blockToSectionCoord(pos.getZ());
      if (sectionY >= this.level.getMinSectionY() && sectionY <= this.level.getMaxSectionY()) {
         LevelChunkSection section = this.getOrLoad(sectionX, sectionY, sectionZ);
         if (section == null) {
            return null;
         } else {
            int relativeX = SectionPos.sectionRelative(pos.getX());
            int relativeY = SectionPos.sectionRelative(pos.getY());
            int relativeZ = SectionPos.sectionRelative(pos.getZ());
            BlockState blockState = section.getBlockState(relativeX, relativeY, relativeZ);
            return this.statePredicate.test(blockState) ? blockState : null;
         }
      } else {
         BlockState air = Blocks.AIR.defaultBlockState();
         return this.statePredicate.test(air) ? air : null;
      }
   }

   @Nullable
   private LevelChunkSection getOrLoad(final int sectionX, final int sectionY, final int sectionZ) {
      int hash = this.getHash(sectionX, sectionY, sectionZ);
      FilteredSectionCache$CacheEntry cacheEntry = this.sectionCache[hash];
      if (cacheEntry == null || cacheEntry.sectionX != sectionX || cacheEntry.sectionY != sectionY || cacheEntry.sectionZ != sectionZ) {
         ChunkAccess chunk = this.level.getChunk(sectionX, sectionZ);
         LevelChunkSection section = chunk.getSection(chunk.getSectionIndexFromSectionY(sectionY));
         cacheEntry = new FilteredSectionCache$CacheEntry(section.maybeHas(this.statePredicate) ? section : null, sectionX, sectionY, sectionZ);
         this.sectionCache[hash] = cacheEntry;
      }

      return cacheEntry.chunkSection;
   }

   private int getHash(final int sectionX, final int sectionY, final int sectionZ) {
      int relativeX = sectionX & 1;
      int relativeY = sectionY & 1;
      int relativeZ = sectionZ & 1;
      return ((relativeZ << 1) + relativeY << 1) + relativeX;
   }
}
