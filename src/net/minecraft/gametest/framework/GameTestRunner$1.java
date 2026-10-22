package net.minecraft.gametest.framework;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Objects;
import net.minecraft.world.level.ChunkPos;

class GameTestRunner$1 implements GameTestListener {
   GameTestRunner$1(
      final GameTestRunner this$0, final MultipleTestTracker val$currentBatchTracker, final GameTestBatch val$currentBatch, final int val$batchIndex
   ) {
      this.val$currentBatchTracker = val$currentBatchTrackerx;
      this.val$currentBatch = val$currentBatchx;
      this.val$batchIndex = val$batchIndexx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   private void testCompleted(final GameTestInfo testInfo) {
      if (this.val$currentBatchTracker.isDone()) {
         this.this$0.batchListeners.forEach(listener -> listener.testBatchFinished(currentBatch));
         LongSet forcedChunks = new LongArraySet(testInfo.getLevel().getForceLoadedChunks());
         forcedChunks.forEach(pos -> testInfo.getLevel().setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
         this.this$0.runBatch(this.val$batchIndex + 1);
      }
   }

   @Override
   public void testStructureLoaded(final GameTestInfo testInfo) {
   }

   @Override
   public void testPassed(final GameTestInfo testInfo, final GameTestRunner runner) {
      testInfo.getTestInstanceBlockEntity().removeBarriers();
      this.testCompleted(testInfo);
   }

   @Override
   public void testFailed(final GameTestInfo testInfo, final GameTestRunner runner) {
      if (this.this$0.haltOnError) {
         this.this$0.endCurrentEnvironment();
         LongSet forcedChunks = new LongArraySet(testInfo.getLevel().getForceLoadedChunks());
         forcedChunks.forEach(pos -> testInfo.getLevel().setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
         GameTestTicker.SINGLETON.clear();
      } else {
         this.testCompleted(testInfo);
      }
   }

   @Override
   public void testAddedForRerun(final GameTestInfo original, final GameTestInfo copy, final GameTestRunner runner) {
   }
}
