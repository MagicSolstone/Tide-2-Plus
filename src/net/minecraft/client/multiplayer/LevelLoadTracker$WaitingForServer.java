package net.minecraft.client.multiplayer;

import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
record LevelLoadTracker$WaitingForServer(LocalPlayer player, ClientLevel level, long timeoutAfter) implements LevelLoadTracker$ClientState {
   @Override
   public LevelLoadTracker$ClientState loadingPacketsReceived() {
      return new LevelLoadTracker$WaitingForPlayerChunk(this.player, this.level, new AtomicBoolean(), this.timeoutAfter);
   }
}
