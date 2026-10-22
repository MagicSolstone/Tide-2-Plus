package net.minecraft.server.level.progress;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public interface LevelLoadListener {
   static LevelLoadListener compose(final LevelLoadListener first, final LevelLoadListener second) {
      return new LevelLoadListener$1(first, second);
   }

   void start(LevelLoadListener$Stage stage, int totalChunks);

   void update(LevelLoadListener$Stage stage, int currentChunks, int totalChunks);

   void finish(LevelLoadListener$Stage stage);

   void updateFocus(ResourceKey<Level> dimension, ChunkPos chunkPos);
}
