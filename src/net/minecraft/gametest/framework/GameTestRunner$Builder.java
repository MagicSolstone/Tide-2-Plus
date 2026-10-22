package net.minecraft.gametest.framework;

import java.util.Collection;
import net.minecraft.server.MinecraftServer;

public class GameTestRunner$Builder {
   private final MinecraftServer server;
   private final GameTestTicker testTicker = GameTestTicker.SINGLETON;
   private GameTestRunner$GameTestBatcher batcher = GameTestBatchFactory.fromGameTestInfo();
   private GameTestRunner$StructureSpawner existingStructureSpawner = GameTestRunner$StructureSpawner.IN_PLACE;
   private GameTestRunner$StructureSpawner newStructureSpawner = GameTestRunner$StructureSpawner.NOT_SET;
   private final Collection<GameTestBatch> batches;
   private boolean haltOnError = false;
   private boolean clearBetweenBatches = false;

   private GameTestRunner$Builder(final Collection<GameTestBatch> batches, final MinecraftServer server) {
      this.batches = batches;
      this.server = server;
   }

   public static GameTestRunner$Builder fromBatches(final Collection<GameTestBatch> batches, final MinecraftServer server) {
      return new GameTestRunner$Builder(batches, server);
   }

   public static GameTestRunner$Builder fromInfo(final Collection<GameTestInfo> tests, final MinecraftServer server) {
      return fromBatches(GameTestBatchFactory.fromGameTestInfo().batch(tests), server);
   }

   public GameTestRunner$Builder haltOnError() {
      this.haltOnError = true;
      return this;
   }

   public GameTestRunner$Builder clearBetweenBatches() {
      this.clearBetweenBatches = true;
      return this;
   }

   public GameTestRunner$Builder newStructureSpawner(final GameTestRunner$StructureSpawner structureSpawner) {
      this.newStructureSpawner = structureSpawner;
      return this;
   }

   public GameTestRunner$Builder existingStructureSpawner(final StructureGridSpawner spawner) {
      this.existingStructureSpawner = spawner;
      return this;
   }

   public GameTestRunner$Builder batcher(final GameTestRunner$GameTestBatcher batcher) {
      this.batcher = batcher;
      return this;
   }

   public GameTestRunner build() {
      return new GameTestRunner(
         this.batcher,
         this.batches,
         this.server,
         this.testTicker,
         this.existingStructureSpawner,
         this.newStructureSpawner,
         this.haltOnError,
         this.clearBetweenBatches
      );
   }
}
