package net.minecraft.world.level.chunk;

import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface LevelChunk$UnsavedListener {
   void setUnsaved(ChunkPos chunkPos);
}
