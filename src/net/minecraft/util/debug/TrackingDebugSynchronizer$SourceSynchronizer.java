package net.minecraft.util.debug;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public class TrackingDebugSynchronizer$SourceSynchronizer<T> extends TrackingDebugSynchronizer<T> {
   private final Map<ChunkPos, TrackingDebugSynchronizer$ValueSource<T>> chunkSources = new HashMap<>();
   private final Map<BlockPos, TrackingDebugSynchronizer$ValueSource<T>> blockEntitySources = new HashMap<>();
   private final Map<UUID, TrackingDebugSynchronizer$ValueSource<T>> entitySources = new HashMap<>();

   public TrackingDebugSynchronizer$SourceSynchronizer(final DebugSubscription<T> subscription) {
      super(subscription);
   }

   @Override
   protected void clear() {
      this.chunkSources.clear();
      this.blockEntitySources.clear();
      this.entitySources.clear();
   }

   @Override
   protected void pollAndSendUpdates(final ServerLevel level) {
      for (Entry<ChunkPos, TrackingDebugSynchronizer$ValueSource<T>> entry : this.chunkSources.entrySet()) {
         DebugSubscription$Update<T> update = entry.getValue().pollUpdate(this.subscription);
         if (update != null) {
            ChunkPos chunkPos = entry.getKey();
            this.sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugChunkValuePacket(chunkPos, update));
         }
      }

      for (Entry<BlockPos, TrackingDebugSynchronizer$ValueSource<T>> entryx : this.blockEntitySources.entrySet()) {
         DebugSubscription$Update<T> update = entryx.getValue().pollUpdate(this.subscription);
         if (update != null) {
            BlockPos blockPos = entryx.getKey();
            ChunkPos chunkPos = ChunkPos.containing(blockPos);
            this.sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugBlockValuePacket(blockPos, update));
         }
      }

      for (Entry<UUID, TrackingDebugSynchronizer$ValueSource<T>> entryxx : this.entitySources.entrySet()) {
         DebugSubscription$Update<T> update = entryxx.getValue().pollUpdate(this.subscription);
         if (update != null) {
            Entity entity = Objects.requireNonNull(level.getEntity(entryxx.getKey()));
            this.sendToPlayersTrackingEntity(level, entity, new ClientboundDebugEntityValuePacket(entity.getId(), update));
         }
      }
   }

   public void registerChunk(final ChunkPos chunkPos, final DebugValueSource$ValueGetter<T> getter) {
      this.chunkSources.put(chunkPos, new TrackingDebugSynchronizer$ValueSource<>(getter));
   }

   public void registerBlockEntity(final BlockPos blockPos, final DebugValueSource$ValueGetter<T> getter) {
      this.blockEntitySources.put(blockPos, new TrackingDebugSynchronizer$ValueSource<>(getter));
   }

   public void registerEntity(final UUID entityId, final DebugValueSource$ValueGetter<T> getter) {
      this.entitySources.put(entityId, new TrackingDebugSynchronizer$ValueSource<>(getter));
   }

   public void dropChunk(final ChunkPos chunkPos) {
      this.chunkSources.remove(chunkPos);
      this.blockEntitySources.keySet().removeIf(chunkPos::contains);
   }

   public void dropBlockEntity(final ServerLevel level, final BlockPos blockPos) {
      TrackingDebugSynchronizer$ValueSource<T> source = this.blockEntitySources.remove(blockPos);
      if (source != null) {
         ChunkPos chunkPos = ChunkPos.containing(blockPos);
         this.sendToPlayersTrackingChunk(level, chunkPos, new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
      }
   }

   public void dropEntity(final Entity entity) {
      this.entitySources.remove(entity.getUUID());
   }

   @Override
   protected void sendInitialChunk(final ServerPlayer player, final ChunkPos chunkPos) {
      TrackingDebugSynchronizer$ValueSource<T> chunkSource = this.chunkSources.get(chunkPos);
      if (chunkSource != null && chunkSource.lastSyncedValue != null) {
         player.connection.send(new ClientboundDebugChunkValuePacket(chunkPos, this.subscription.packUpdate(chunkSource.lastSyncedValue)));
      }

      for (Entry<BlockPos, TrackingDebugSynchronizer$ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
         T lastValue = entry.getValue().lastSyncedValue;
         if (lastValue != null) {
            BlockPos blockPos = entry.getKey();
            if (chunkPos.contains(blockPos)) {
               player.connection.send(new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(lastValue)));
            }
         }
      }
   }

   @Override
   protected void sendInitialEntity(final ServerPlayer player, final Entity entity) {
      TrackingDebugSynchronizer$ValueSource<T> source = this.entitySources.get(entity.getUUID());
      if (source != null && source.lastSyncedValue != null) {
         player.connection.send(new ClientboundDebugEntityValuePacket(entity.getId(), this.subscription.packUpdate(source.lastSyncedValue)));
      }
   }
}
