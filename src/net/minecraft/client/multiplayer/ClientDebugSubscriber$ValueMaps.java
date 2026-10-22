package net.minecraft.client.multiplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

@Environment(EnvType.CLIENT)
class ClientDebugSubscriber$ValueMaps<V> {
   private final ClientDebugSubscriber$ValueMap<ChunkPos, V> chunkValues = new ClientDebugSubscriber$ValueMap<>();
   private final ClientDebugSubscriber$ValueMap<BlockPos, V> blockValues = new ClientDebugSubscriber$ValueMap<>();
   private final ClientDebugSubscriber$ValueMap<UUID, V> entityValues = new ClientDebugSubscriber$ValueMap<>();
   private final List<ClientDebugSubscriber$ValueWrapper<V>> events = new ArrayList<>();

   private ClientDebugSubscriber$ValueMaps() {
   }

   public void purgeExpired(final long gameTime) {
      Predicate<ClientDebugSubscriber$ValueWrapper<V>> expiredPredicate = v -> v.hasExpired(gameTime);
      this.chunkValues.removeValues(expiredPredicate);
      this.blockValues.removeValues(expiredPredicate);
      this.entityValues.removeValues(expiredPredicate);
      this.events.removeIf(expiredPredicate);
   }

   public void dropChunkAndBlocks(final ChunkPos chunkPos) {
      this.chunkValues.removeKey(chunkPos);
      this.blockValues.removeKeys(chunkPos::contains);
   }
}
