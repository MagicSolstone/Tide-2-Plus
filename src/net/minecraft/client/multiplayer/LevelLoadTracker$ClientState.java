package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
sealed interface LevelLoadTracker$ClientState
   permits LevelLoadTracker$ClientLevelReady,
   LevelLoadTracker$WaitingForPlayerChunk,
   LevelLoadTracker$WaitingForServer {
   default LevelLoadTracker$ClientState tick() {
      return this;
   }

   default LevelLoadTracker$ClientState loadingPacketsReceived() {
      return this;
   }
}
