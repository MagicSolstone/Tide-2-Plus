package net.minecraft.world.level.blockscan;

import net.minecraft.world.level.chunk.LevelChunkSection;
import org.jspecify.annotations.Nullable;

record FilteredSectionCache$CacheEntry(@Nullable LevelChunkSection chunkSection, int sectionX, int sectionY, int sectionZ) {
}
