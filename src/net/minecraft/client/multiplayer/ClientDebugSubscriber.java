package net.minecraft.client.multiplayer;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundDebugSubscriptionRequestPacket;
import net.minecraft.util.debug.DebugSubscription;
import net.minecraft.util.debug.DebugSubscription$Event;
import net.minecraft.util.debug.DebugSubscription$Update;
import net.minecraft.util.debug.DebugSubscriptions;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.util.debugchart.RemoteDebugSampleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ClientDebugSubscriber {
   private final ClientPacketListener connection;
   private final DebugScreenOverlay debugScreenOverlay;
   private Set<DebugSubscription<?>> remoteSubscriptions = Set.of();
   private final Map<DebugSubscription<?>, ClientDebugSubscriber$ValueMaps<?>> valuesBySubscription = new HashMap<>();

   public ClientDebugSubscriber(final ClientPacketListener connection, final DebugScreenOverlay debugScreenOverlay) {
      this.debugScreenOverlay = debugScreenOverlay;
      this.connection = connection;
   }

   private static void addFlag(final Set<DebugSubscription<?>> output, final DebugSubscription<?> subscription, final boolean flag) {
      if (flag) {
         output.add(subscription);
      }
   }

   private Set<DebugSubscription<?>> requestedSubscriptions() {
      Set<DebugSubscription<?>> subscriptions = new ReferenceOpenHashSet();
      addFlag(subscriptions, RemoteDebugSampleType.TICK_TIME.subscription(), this.debugScreenOverlay.showFpsCharts());
      if (SharedConstants.DEBUG_ENABLED) {
         addFlag(subscriptions, DebugSubscriptions.BEES, SharedConstants.DEBUG_BEES);
         addFlag(subscriptions, DebugSubscriptions.BEE_HIVES, SharedConstants.DEBUG_BEES);
         addFlag(subscriptions, DebugSubscriptions.BRAINS, SharedConstants.DEBUG_BRAIN);
         addFlag(subscriptions, DebugSubscriptions.BREEZES, SharedConstants.DEBUG_BREEZE_MOB);
         addFlag(subscriptions, DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS, SharedConstants.DEBUG_ENTITY_BLOCK_INTERSECTION);
         addFlag(subscriptions, DebugSubscriptions.ENTITY_PATHS, SharedConstants.DEBUG_PATHFINDING);
         addFlag(subscriptions, DebugSubscriptions.GAME_EVENTS, SharedConstants.DEBUG_GAME_EVENT_LISTENERS);
         addFlag(subscriptions, DebugSubscriptions.GAME_EVENT_LISTENERS, SharedConstants.DEBUG_GAME_EVENT_LISTENERS);
         addFlag(subscriptions, DebugSubscriptions.GOAL_SELECTORS, SharedConstants.DEBUG_GOAL_SELECTOR || SharedConstants.DEBUG_BEES);
         addFlag(subscriptions, DebugSubscriptions.NEIGHBOR_UPDATES, SharedConstants.DEBUG_NEIGHBORSUPDATE);
         addFlag(subscriptions, DebugSubscriptions.POIS, SharedConstants.DEBUG_POI);
         addFlag(subscriptions, DebugSubscriptions.RAIDS, SharedConstants.DEBUG_RAIDS);
         addFlag(subscriptions, DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS, SharedConstants.DEBUG_EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER);
         addFlag(subscriptions, DebugSubscriptions.STRUCTURES, SharedConstants.DEBUG_STRUCTURES);
         addFlag(subscriptions, DebugSubscriptions.VILLAGE_SECTIONS, SharedConstants.DEBUG_VILLAGE_SECTIONS);
      }

      return subscriptions;
   }

   public void clear() {
      this.remoteSubscriptions = Set.of();
      this.dropLevel();
   }

   public void tick(final long gameTime) {
      Set<DebugSubscription<?>> newSubscriptions = this.requestedSubscriptions();
      if (!newSubscriptions.equals(this.remoteSubscriptions)) {
         this.remoteSubscriptions = newSubscriptions;
         this.onSubscriptionsChanged(newSubscriptions);
      }

      this.valuesBySubscription.forEach((subscription, valueMaps) -> {
         if (subscription.expireAfterTicks() != 0) {
            valueMaps.purgeExpired(gameTime);
         }
      });
   }

   private void onSubscriptionsChanged(final Set<DebugSubscription<?>> newSubscriptions) {
      this.valuesBySubscription.keySet().retainAll(newSubscriptions);
      this.initializeSubscriptions(newSubscriptions);
      this.connection.send(new ServerboundDebugSubscriptionRequestPacket(newSubscriptions));
   }

   private void initializeSubscriptions(final Set<DebugSubscription<?>> newSubscriptions) {
      for (DebugSubscription<?> subscription : newSubscriptions) {
         this.valuesBySubscription.computeIfAbsent(subscription, s -> new ClientDebugSubscriber$ValueMaps());
      }
   }

   @Nullable
   private <V> ClientDebugSubscriber$ValueMaps<V> getValueMaps(final DebugSubscription<V> subscription) {
      return (ClientDebugSubscriber$ValueMaps<V>)this.valuesBySubscription.get(subscription);
   }

   @Nullable
   private <K, V> ClientDebugSubscriber$ValueMap<K, V> getValueMap(
      final DebugSubscription<V> subscription, final ClientDebugSubscriber$ValueMapType<K, V> mapType
   ) {
      ClientDebugSubscriber$ValueMaps<V> maps = this.getValueMaps(subscription);
      return maps != null ? mapType.get(maps) : null;
   }

   @Nullable
   private <K, V> V getValue(final DebugSubscription<V> subscription, final K key, final ClientDebugSubscriber$ValueMapType<K, V> type) {
      ClientDebugSubscriber$ValueMap<K, V> values = this.getValueMap(subscription, type);
      return values != null ? values.getValue(key) : null;
   }

   public DebugValueAccess createDebugValueAccess(final Level level) {
      return new ClientDebugSubscriber$1(this, level);
   }

   public <T> void updateChunk(final long gameTime, final ChunkPos chunkPos, final DebugSubscription$Update<T> update) {
      this.updateMap(gameTime, chunkPos, update, chunks());
   }

   public <T> void updateBlock(final long gameTime, final BlockPos blockPos, final DebugSubscription$Update<T> update) {
      this.updateMap(gameTime, blockPos, update, blocks());
   }

   public <T> void updateEntity(final long gameTime, final Entity entity, final DebugSubscription$Update<T> update) {
      this.updateMap(gameTime, entity.getUUID(), update, entities());
   }

   public <T> void pushEvent(final long gameTime, final DebugSubscription$Event<T> event) {
      ClientDebugSubscriber$ValueMaps<T> values = this.getValueMaps(event.subscription());
      if (values != null) {
         values.events.add(new ClientDebugSubscriber$ValueWrapper<>(event.value(), gameTime + event.subscription().expireAfterTicks()));
      }
   }

   private <K, V> void updateMap(
      final long gameTime, final K key, final DebugSubscription$Update<V> update, final ClientDebugSubscriber$ValueMapType<K, V> type
   ) {
      ClientDebugSubscriber$ValueMap<K, V> values = this.getValueMap(update.subscription(), type);
      if (values != null) {
         values.apply(gameTime, key, update);
      }
   }

   private <K, V> void forEachValue(
      final DebugSubscription<V> subscription, final ClientDebugSubscriber$ValueMapType<K, V> type, final BiConsumer<K, V> consumer
   ) {
      ClientDebugSubscriber$ValueMap<K, V> values = this.getValueMap(subscription, type);
      if (values != null) {
         values.forEach(consumer);
      }
   }

   public void dropLevel() {
      this.valuesBySubscription.clear();
      this.initializeSubscriptions(this.remoteSubscriptions);
   }

   public void dropChunk(final ChunkPos chunkPos) {
      if (!this.valuesBySubscription.isEmpty()) {
         for (ClientDebugSubscriber$ValueMaps<?> values : this.valuesBySubscription.values()) {
            values.dropChunkAndBlocks(chunkPos);
         }
      }
   }

   public void dropEntity(final Entity entity) {
      if (!this.valuesBySubscription.isEmpty()) {
         for (ClientDebugSubscriber$ValueMaps<?> values : this.valuesBySubscription.values()) {
            values.entityValues.removeKey(entity.getUUID());
         }
      }
   }

   private static <T> ClientDebugSubscriber$ValueMapType<UUID, T> entities() {
      return v -> v.entityValues;
   }

   private static <T> ClientDebugSubscriber$ValueMapType<BlockPos, T> blocks() {
      return v -> v.blockValues;
   }

   private static <T> ClientDebugSubscriber$ValueMapType<ChunkPos, T> chunks() {
      return v -> v.chunkValues;
   }
}
