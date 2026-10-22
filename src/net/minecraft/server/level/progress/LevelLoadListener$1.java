package net.minecraft.server.level.progress;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

class LevelLoadListener$1 implements LevelLoadListener {
   LevelLoadListener$1(final LevelLoadListener val$first, final LevelLoadListener val$second) {
      this.val$first = val$firstx;
      this.val$second = val$secondx;
   }

   @Override
   public void start(final LevelLoadListener$Stage stage, final int totalChunks) {
      this.val$first.start(stage, totalChunks);
      this.val$second.start(stage, totalChunks);
   }

   @Override
   public void update(final LevelLoadListener$Stage stage, final int currentChunks, final int totalChunks) {
      this.val$first.update(stage, currentChunks, totalChunks);
      this.val$second.update(stage, currentChunks, totalChunks);
   }

   @Override
   public void finish(final LevelLoadListener$Stage stage) {
      this.val$first.finish(stage);
      this.val$second.finish(stage);
   }

   @Override
   public void updateFocus(final ResourceKey<Level> dimension, final ChunkPos chunkPos) {
      this.val$first.updateFocus(dimension, chunkPos);
      this.val$second.updateFocus(dimension, chunkPos);
   }
}
