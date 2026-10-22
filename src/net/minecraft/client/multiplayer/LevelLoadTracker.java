package net.minecraft.client.multiplayer;

import com.mojang.logging.LogUtils;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.progress.ChunkLoadStatusView;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.level.progress.LevelLoadListener$Stage;
import net.minecraft.server.level.progress.LevelLoadProgressTracker;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class LevelLoadTracker implements LevelLoadListener {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final long CLIENT_WAIT_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(30L);
   public static final long LEVEL_LOAD_CLOSE_DELAY_MS = 500L;
   private final LevelLoadProgressTracker serverProgressTracker = new LevelLoadProgressTracker(true);
   @Nullable
   private ChunkLoadStatusView serverChunkStatusView;
   @Nullable
   private volatile LevelLoadListener$Stage serverStage;
   @Nullable
   private LevelLoadTracker$ClientState clientState;
   private final long closeDelayMs;

   public LevelLoadTracker() {
      this(0L);
   }

   public LevelLoadTracker(final long closeDelayMs) {
      this.closeDelayMs = closeDelayMs;
   }

   public void setServerChunkStatusView(final ChunkLoadStatusView serverChunkStatusView) {
      this.serverChunkStatusView = serverChunkStatusView;
   }

   public void startClientLoad(final LocalPlayer player, final ClientLevel level) {
      this.clientState = new LevelLoadTracker$WaitingForServer(player, level, Util.getMillis() + CLIENT_WAIT_TIMEOUT_MS);
   }

   public void tickClientLoad() {
      if (this.clientState != null) {
         this.clientState = this.clientState.tick();
      }
   }

   public boolean isLevelReady() {
      if (this.clientState instanceof LevelLoadTracker$ClientLevelReady(long var10)) {
         long var5 = var10;
         if (true && Util.getMillis() >= var5 + this.closeDelayMs) {
            return true;
         }
      }

      return false;
   }

   public void loadingPacketsReceived() {
      if (this.clientState != null) {
         this.clientState = this.clientState.loadingPacketsReceived();
      }
   }

   @Override
   public void start(final LevelLoadListener$Stage stage, final int totalChunks) {
      this.serverProgressTracker.start(stage, totalChunks);
      this.serverStage = stage;
   }

   @Override
   public void update(final LevelLoadListener$Stage stage, final int currentChunks, final int totalChunks) {
      this.serverProgressTracker.update(stage, currentChunks, totalChunks);
   }

   @Override
   public void finish(final LevelLoadListener$Stage stage) {
      this.serverProgressTracker.finish(stage);
   }

   @Override
   public void updateFocus(final ResourceKey<Level> dimension, final ChunkPos chunkPos) {
      if (this.serverChunkStatusView != null) {
         this.serverChunkStatusView.moveTo(dimension, chunkPos);
      }
   }

   @Nullable
   public ChunkLoadStatusView statusView() {
      return this.serverChunkStatusView;
   }

   public float serverProgress() {
      return this.serverProgressTracker.get();
   }

   public boolean hasProgress() {
      return this.serverStage != null;
   }

   @Nullable
   public Runnable getPlayerCompiledSectionCallback() {
      return this.clientState instanceof LevelLoadTracker$WaitingForPlayerChunk waitingForPlayerChunk
         ? () -> waitingForPlayerChunk.playerSectionReady().set(true)
         : null;
   }
}
