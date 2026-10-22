package net.minecraft.server.level;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.UpdateInterval;
import net.minecraft.world.phys.Vec3;

class ChunkMap$TrackedEntity implements ServerEntity$Synchronizer {
   private final ServerEntity serverEntity;
   private final Entity entity;
   private final int range;
   private SectionPos lastSectionPos;
   private final Set<ServerPlayerConnection> seenBy;

   public ChunkMap$TrackedEntity(final ChunkMap this$0, final Entity entity, final int range, final UpdateInterval updateInterval, final boolean trackDelta) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.seenBy = Sets.newIdentityHashSet();
      this.serverEntity = new ServerEntity(this$0x.level, entity, updateInterval, trackDelta, this);
      this.entity = entity;
      this.range = range;
      this.lastSectionPos = SectionPos.of(entity);
   }

   @Override
   public boolean equals(final Object obj) {
      return obj instanceof ChunkMap$TrackedEntity trackedEntity ? trackedEntity.entity.getId() == this.entity.getId() : false;
   }

   @Override
   public int hashCode() {
      return this.entity.getId();
   }

   @Override
   public void sendToTrackingPlayers(final Packet<? super ClientGamePacketListener> packet) {
      for (ServerPlayerConnection connection : this.seenBy) {
         connection.send(packet);
      }
   }

   @Override
   public void sendToTrackingPlayersAndSelf(final Packet<? super ClientGamePacketListener> packet) {
      this.sendToTrackingPlayers(packet);
      if (this.entity instanceof ServerPlayer player) {
         player.connection.send(packet);
      }
   }

   @Override
   public void sendToTrackingPlayersFiltered(final Packet<? super ClientGamePacketListener> packet, final Predicate<ServerPlayer> targetPredicate) {
      for (ServerPlayerConnection connection : this.seenBy) {
         if (targetPredicate.test(connection.getPlayer())) {
            connection.send(packet);
         }
      }
   }

   public void broadcastRemoved() {
      for (ServerPlayerConnection connection : this.seenBy) {
         this.serverEntity.removePairing(connection.getPlayer());
      }
   }

   public void removePlayer(final ServerPlayer player) {
      if (this.seenBy.remove(player.connection)) {
         this.serverEntity.removePairing(player);
         if (this.seenBy.isEmpty()) {
            this.this$0.level.debugSynchronizers().dropEntity(this.entity);
         }
      }
   }

   public void updatePlayer(final ServerPlayer player) {
      if (player != this.entity) {
         Vec3 deltaToPlayer = player.position().subtract(this.entity.position());
         int playerViewDistance = this.this$0.getPlayerViewDistance(player);
         double visibleRange = Math.min(this.getEffectiveRange(), playerViewDistance * 16);
         double distanceSquared = deltaToPlayer.x * deltaToPlayer.x + deltaToPlayer.z * deltaToPlayer.z;
         double rangeSquared = visibleRange * visibleRange;
         boolean visibleToPlayer = distanceSquared <= rangeSquared
            && this.entity.broadcastToPlayer(player)
            && this.this$0.isChunkTracked(player, this.entity.chunkPosition().x(), this.entity.chunkPosition().z());
         if (visibleToPlayer) {
            if (this.seenBy.add(player.connection)) {
               this.serverEntity.addPairing(player);
               if (this.seenBy.size() == 1) {
                  this.this$0.level.debugSynchronizers().registerEntity(this.entity);
               }

               this.this$0.level.debugSynchronizers().startTrackingEntity(player, this.entity);
            }
         } else {
            this.removePlayer(player);
         }
      }
   }

   private int scaledRange(final int range) {
      return this.this$0.level.getServer().getScaledTrackingDistance(range);
   }

   private int getEffectiveRange() {
      int effectiveRange = this.range;

      for (Entity passenger : this.entity.getIndirectPassengers()) {
         int passengerRange = passenger.getType().clientTrackingRange() * 16;
         if (passengerRange > effectiveRange) {
            effectiveRange = passengerRange;
         }
      }

      return this.scaledRange(effectiveRange);
   }

   public void updatePlayers(final List<ServerPlayer> players) {
      for (ServerPlayer player : players) {
         this.updatePlayer(player);
      }
   }
}
