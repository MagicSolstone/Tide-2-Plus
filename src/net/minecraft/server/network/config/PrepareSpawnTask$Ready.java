package net.minecraft.server.network.config;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.util.ProblemReporter$ScopedCollector;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

final class PrepareSpawnTask$Ready implements PrepareSpawnTask$State {
   private final ServerLevel spawnLevel;
   private final Vec3 spawnPosition;
   private final Vec2 spawnAngle;

   private PrepareSpawnTask$Ready(final PrepareSpawnTask this$0, final ServerLevel spawnLevel, final Vec3 spawnPosition, final Vec2 spawnAngle) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.spawnLevel = spawnLevel;
      this.spawnPosition = spawnPosition;
      this.spawnAngle = spawnAngle;
   }

   public void keepAlive() {
      this.spawnLevel.getChunkSource().addTicketWithRadius(TicketType.PLAYER_SPAWN, ChunkPos.containing(BlockPos.containing(this.spawnPosition)), 3);
   }

   public ServerPlayer spawn(final Connection connection, final CommonListenerCookie cookie) {
      ChunkPos spawnChunk = ChunkPos.containing(BlockPos.containing(this.spawnPosition));
      this.spawnLevel.waitForEntities(spawnChunk, 3);
      ServerPlayer player = new ServerPlayer(this.this$0.server, this.spawnLevel, cookie.gameProfile(), cookie.clientInformation());

      ServerPlayer var7;
      try (ProblemReporter$ScopedCollector reporter = new ProblemReporter$ScopedCollector(player.problemPath(), PrepareSpawnTask.LOGGER)) {
         Optional<ValueInput> input = this.this$0
            .server
            .getPlayerList()
            .loadPlayerData(this.this$0.nameAndId)
            .map(tag -> TagValueInput.create(reporter, this.this$0.server.registryAccess(), tag));
         input.ifPresent(player::load);
         player.snapTo(this.spawnPosition, this.spawnAngle.x, this.spawnAngle.y);
         this.this$0.server.getPlayerList().placeNewPlayer(connection, player, cookie);
         input.ifPresent(tag -> {
            player.loadAndSpawnEnderPearls(tag);
            player.loadAndSpawnParentVehicle(tag);
         });
         var7 = player;
      }

      return var7;
   }
}
