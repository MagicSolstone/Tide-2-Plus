package net.minecraft.util.debug;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public abstract class TrackingDebugSynchronizer<T> {
   protected final DebugSubscription<T> subscription;
   private final Set<UUID> subscribedPlayers = new ObjectOpenHashSet();

   public TrackingDebugSynchronizer(final DebugSubscription<T> subscription) {
      this.subscription = subscription;
   }

   public final void tick(final ServerLevel level) {
      for (ServerPlayer player : level.players()) {
         boolean wasSubscribed = this.subscribedPlayers.contains(player.getUUID());
         boolean isSubscribed = player.debugSubscriptions().contains(this.subscription);
         if (isSubscribed != wasSubscribed) {
            if (isSubscribed) {
               this.addSubscriber(player);
            } else {
               this.subscribedPlayers.remove(player.getUUID());
            }
         }
      }

      this.subscribedPlayers.removeIf(id -> level.getPlayerByUUID(id) == null);
      if (!this.subscribedPlayers.isEmpty()) {
         this.pollAndSendUpdates(level);
      }
   }

   private void addSubscriber(final ServerPlayer player) {
      this.subscribedPlayers.add(player.getUUID());
      player.getChunkTrackingView().forEach(chunkPos -> {
         if (!player.connection.chunkSender.isPending(chunkPos.pack())) {
            this.startTrackingChunk(player, chunkPos);
         }
      });
      player.level().getChunkSource().chunkMap.forEachEntityTrackedBy(player, entity -> this.startTrackingEntity(player, entity));
   }

   protected final void sendToPlayersTrackingChunk(final ServerLevel level, final ChunkPos trackedChunk, final Packet<? super ClientGamePacketListener> packet) {
      ChunkMap chunkMap = level.getChunkSource().chunkMap;

      for (UUID playerId : this.subscribedPlayers) {
         if (level.getPlayerByUUID(playerId) instanceof ServerPlayer player && chunkMap.isChunkTracked(player, trackedChunk.x(), trackedChunk.z())) {
            player.connection.send(packet);
         }
      }
   }

   protected final void sendToPlayersTrackingEntity(final ServerLevel level, final Entity trackedEntity, final Packet<? super ClientGamePacketListener> packet) {
      ChunkMap chunkMap = level.getChunkSource().chunkMap;
      chunkMap.sendToTrackingPlayersFiltered(trackedEntity, packet, player -> this.subscribedPlayers.contains(player.getUUID()));
   }

   public final void startTrackingChunk(final ServerPlayer player, final ChunkPos chunkPos) {
      if (this.subscribedPlayers.contains(player.getUUID())) {
         this.sendInitialChunk(player, chunkPos);
      }
   }

   public final void startTrackingEntity(final ServerPlayer player, final Entity entity) {
      if (this.subscribedPlayers.contains(player.getUUID())) {
         this.sendInitialEntity(player, entity);
      }
   }

   protected void clear() {
   }

   protected void pollAndSendUpdates(final ServerLevel level) {
   }

   protected void sendInitialChunk(final ServerPlayer player, final ChunkPos chunkPos) {
   }

   protected void sendInitialEntity(final ServerPlayer player, final Entity entity) {
   }
}
