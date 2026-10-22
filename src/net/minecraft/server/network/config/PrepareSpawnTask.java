package net.minecraft.server.network.config;

import com.mojang.logging.LogUtils;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayer$SavedPosition;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ConfigurationTask$Type;
import net.minecraft.server.players.NameAndId;
import net.minecraft.util.ProblemReporter$ScopedCollector;
import net.minecraft.world.level.storage.LevelData$RespawnData;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class PrepareSpawnTask implements ConfigurationTask {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final ConfigurationTask$Type TYPE = new ConfigurationTask$Type("prepare_spawn");
   public static final int PREPARE_CHUNK_RADIUS = 3;
   private final MinecraftServer server;
   private final NameAndId nameAndId;
   private final LevelLoadListener loadListener;
   @Nullable
   private PrepareSpawnTask$State state;

   public PrepareSpawnTask(final MinecraftServer server, final NameAndId nameAndId) {
      this.server = server;
      this.nameAndId = nameAndId;
      this.loadListener = server.getLevelLoadListener();
   }

   @Override
   public void start(final Consumer<Packet<?>> connection) {
      try (ProblemReporter$ScopedCollector reporter = new ProblemReporter$ScopedCollector(LOGGER)) {
         Optional<ValueInput> loadedData = this.server
            .getPlayerList()
            .loadPlayerData(this.nameAndId)
            .map(tag -> TagValueInput.create(reporter, this.server.registryAccess(), tag));
         ServerPlayer$SavedPosition loadedPosition = loadedData.<ServerPlayer$SavedPosition>flatMap(tag -> tag.read(ServerPlayer$SavedPosition.MAP_CODEC))
            .orElse(ServerPlayer$SavedPosition.EMPTY);
         LevelData$RespawnData respawnData = this.server.getWorldData().overworldData().getRespawnData();
         ServerLevel spawnLevel = loadedPosition.dimension().map(this.server::getLevel).orElseGet(() -> {
            ServerLevel spawnDataLevel = this.server.getLevel(respawnData.dimension());
            return spawnDataLevel != null ? spawnDataLevel : this.server.overworld();
         });
         CompletableFuture<Vec3> spawnPosition = loadedPosition.position()
            .map(CompletableFuture::completedFuture)
            .orElseGet(() -> PlayerSpawnFinder.findSpawn(spawnLevel, respawnData.pos()));
         Vec2 spawnAngle = loadedPosition.rotation().orElse(new Vec2(respawnData.yaw(), respawnData.pitch()));
         this.state = new PrepareSpawnTask$Preparing(this, spawnLevel, spawnPosition, spawnAngle);
      }
   }

   @Override
   public boolean tick() {
      return switch (this.state) {
         case null -> false;
         case PrepareSpawnTask$Preparing preparing -> {
            PrepareSpawnTask$Ready ready = preparing.tick();
            if (ready != null) {
               this.state = ready;
               yield true;
            } else {
               yield false;
            }
         }
         case PrepareSpawnTask$Ready ignored -> true;
         default -> throw new MatchException(null, null);
      };
   }

   public ServerPlayer spawnPlayer(final Connection connection, final CommonListenerCookie cookie) {
      if (this.state instanceof PrepareSpawnTask$Ready ready) {
         return ready.spawn(connection, cookie);
      } else {
         throw new IllegalStateException("Player spawn was not ready");
      }
   }

   public void keepAlive() {
      if (this.state instanceof PrepareSpawnTask$Ready ready) {
         ready.keepAlive();
      }
   }

   public void close() {
      if (this.state instanceof PrepareSpawnTask$Preparing preparing) {
         preparing.cancel();
      }

      this.state = null;
   }

   @Override
   public ConfigurationTask$Type type() {
      return TYPE;
   }
}
