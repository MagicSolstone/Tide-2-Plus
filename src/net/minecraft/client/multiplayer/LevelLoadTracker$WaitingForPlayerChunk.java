package net.minecraft.client.multiplayer;

import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
record LevelLoadTracker$WaitingForPlayerChunk(LocalPlayer player, ClientLevel level, AtomicBoolean playerSectionReady, long timeoutAfter)
   implements LevelLoadTracker$ClientState {
   @Override
   public LevelLoadTracker$ClientState tick() {
      return (LevelLoadTracker$ClientState)(this.isReady() ? new LevelLoadTracker$ClientLevelReady(Util.getMillis()) : this);
   }

   private boolean isReady() {
      if (Util.getMillis() > this.timeoutAfter) {
         LevelLoadTracker.LOGGER.warn("Timed out while waiting for the client to load chunks, letting the player into the world anyway");
         return true;
      } else {
         BlockPos cameraPos = Minecraft.getInstance().gameRenderer.mainCamera().blockPosition();
         return !this.level.isOutsideBuildHeight(cameraPos.getY()) && !this.player.isSpectator() && this.player.isAlive()
            ? this.playerSectionReady.get()
            : true;
      }
   }
}
