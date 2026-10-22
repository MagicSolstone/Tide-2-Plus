package net.minecraft.gametest.framework;

import java.util.Optional;
import net.minecraft.server.MinecraftServer;

public interface GameTestRunner$StructureSpawner {
   GameTestRunner$StructureSpawner IN_PLACE = testInfo -> Optional.ofNullable(testInfo.prepareTestStructure()).map(e -> e.startExecution(1));
   GameTestRunner$StructureSpawner NOT_SET = testInfo -> Optional.empty();

   Optional<GameTestInfo> spawnStructure(GameTestInfo testInfo);

   default void onBatchStart(final MinecraftServer server) {
   }
}
