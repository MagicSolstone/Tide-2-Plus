package net.minecraft.world.level;

import java.util.function.Consumer;
import net.minecraft.world.level.chunk.LevelChunk;

@FunctionalInterface
public interface NaturalSpawner$ChunkGetter {
   void query(final long chunkKey, Consumer<LevelChunk> output);
}
