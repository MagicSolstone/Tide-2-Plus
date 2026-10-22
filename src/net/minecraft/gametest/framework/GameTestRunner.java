package net.minecraft.gametest.framework;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class GameTestRunner {
   public static final int DEFAULT_TESTS_PER_ROW = 8;
   private static final Logger LOGGER = LogUtils.getLogger();
   private final MinecraftServer server;
   private final GameTestTicker testTicker;
   private final List<GameTestInfo> allTestInfos;
   private ImmutableList<GameTestBatch> batches;
   private final List<GameTestBatchListener> batchListeners = Lists.newArrayList();
   private final List<GameTestInfo> scheduledForRerun = Lists.newArrayList();
   private final GameTestRunner$GameTestBatcher testBatcher;
   private boolean stopped = true;
   @Nullable
   private TestEnvironmentDefinition$Activation<?> currentEnvironment;
   private final GameTestRunner$StructureSpawner existingStructureSpawner;
   private final GameTestRunner$StructureSpawner newStructureSpawner;
   private final boolean haltOnError;
   private final boolean clearBetweenBatches;

   protected GameTestRunner(
      final GameTestRunner$GameTestBatcher batcher,
      final Collection<GameTestBatch> batches,
      final MinecraftServer server,
      final GameTestTicker testTicker,
      final GameTestRunner$StructureSpawner existingStructureSpawner,
      final GameTestRunner$StructureSpawner newStructureSpawner,
      final boolean haltOnError,
      final boolean clearBetweenBatches
   ) {
      this.server = server;
      this.testTicker = testTicker;
      this.testBatcher = batcher;
      this.existingStructureSpawner = existingStructureSpawner;
      this.newStructureSpawner = newStructureSpawner;
      this.batches = ImmutableList.copyOf(batches);
      this.haltOnError = haltOnError;
      this.clearBetweenBatches = clearBetweenBatches;
      this.allTestInfos = this.batches.stream().flatMap(batch -> batch.gameTestInfos().stream()).collect(Util.toMutableList());
      testTicker.setRunner(this);
      this.allTestInfos.forEach(info -> info.addListener(new ReportGameListener()));
   }

   public List<GameTestInfo> getTestInfos() {
      return this.allTestInfos;
   }

   public void start() {
      this.stopped = false;
      this.runBatch(0);
   }

   public void stop() {
      this.stopped = true;
      if (this.currentEnvironment != null) {
         this.endCurrentEnvironment();
      }
   }

   public void rerunTest(final GameTestInfo info) {
      GameTestInfo copy = info.copyReset();
      info.getListeners().forEach(listener -> listener.testAddedForRerun(info, copy, this));
      this.allTestInfos.add(copy);
      this.scheduledForRerun.add(copy);
      if (this.stopped) {
         this.runScheduledRerunTests();
      }
   }

   private void runBatch(final int batchIndex) {
      if (batchIndex >= this.batches.size()) {
         this.endCurrentEnvironment();
         this.runScheduledRerunTests();
      } else {
         if (batchIndex > 0 && this.clearBetweenBatches) {
            GameTestBatch lastBatch = (GameTestBatch)this.batches.get(batchIndex - 1);
            lastBatch.gameTestInfos().forEach(gameTestInfo -> {
               ServerLevel level = gameTestInfo.getLevel();
               TestInstanceBlockEntity testInstanceBlockEntity = gameTestInfo.getTestInstanceBlockEntity();
               StructureUtils.clearSpaceForStructure(testInstanceBlockEntity.getTestBoundingBox(), level);
               level.destroyBlock(testInstanceBlockEntity.getBlockPos(), false);
            });
         }

         GameTestBatch currentBatch = (GameTestBatch)this.batches.get(batchIndex);
         this.existingStructureSpawner.onBatchStart(this.server);
         this.newStructureSpawner.onBatchStart(this.server);
         Collection<GameTestInfo> testInfosForThisBatch = this.createStructuresForBatch(currentBatch.gameTestInfos());
         LOGGER.info(
            "Running test environment '{}' batch {} ({} tests)...",
            new Object[]{currentBatch.environment().getRegisteredName(), currentBatch.index(), testInfosForThisBatch.size()}
         );
         this.endCurrentEnvironment();
         this.currentEnvironment = TestEnvironmentDefinition.activate(currentBatch.environment().value(), this.server.getLevel(currentBatch.dimension()));
         this.batchListeners.forEach(listener -> listener.testBatchStarting(currentBatch));
         MultipleTestTracker currentBatchTracker = new MultipleTestTracker();
         testInfosForThisBatch.forEach(currentBatchTracker::addTestToTrack);
         currentBatchTracker.addListener(new GameTestRunner$1(this, currentBatchTracker, currentBatch, batchIndex));
         testInfosForThisBatch.forEach(this.testTicker::add);
      }
   }

   private void endCurrentEnvironment() {
      if (this.currentEnvironment != null) {
         this.currentEnvironment.teardown();
         this.currentEnvironment = null;
      }
   }

   private void runScheduledRerunTests() {
      if (!this.scheduledForRerun.isEmpty()) {
         LOGGER.info("Starting re-run of tests: {}", this.scheduledForRerun.stream().map(info -> info.id().toString()).collect(Collectors.joining(", ")));
         this.batches = ImmutableList.copyOf(this.testBatcher.batch(this.scheduledForRerun));
         this.scheduledForRerun.clear();
         this.stopped = false;
         this.runBatch(0);
      } else {
         this.batches = ImmutableList.of();
         this.stopped = true;
      }
   }

   public void addListener(final GameTestBatchListener listener) {
      this.batchListeners.add(listener);
   }

   private Collection<GameTestInfo> createStructuresForBatch(final Collection<GameTestInfo> batch) {
      return batch.stream().map(this::spawn).flatMap(Optional::stream).toList();
   }

   private Optional<GameTestInfo> spawn(final GameTestInfo testInfo) {
      return testInfo.getTestBlockPos() == null ? this.newStructureSpawner.spawnStructure(testInfo) : this.existingStructureSpawner.spawnStructure(testInfo);
   }
}
