package net.minecraft.client.multiplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.debug.DebugSubscription$Update;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ClientDebugSubscriber$ValueMap<K, V> {
   private final Map<K, ClientDebugSubscriber$ValueWrapper<V>> values = new HashMap<>();

   private ClientDebugSubscriber$ValueMap() {
   }

   public void removeValues(final Predicate<ClientDebugSubscriber$ValueWrapper<V>> predicate) {
      this.values.values().removeIf(predicate);
   }

   public void removeKey(final K key) {
      this.values.remove(key);
   }

   public void removeKeys(final Predicate<K> predicate) {
      this.values.keySet().removeIf(predicate);
   }

   @Nullable
   public V getValue(final K key) {
      ClientDebugSubscriber$ValueWrapper<V> result = this.values.get(key);
      return result != null ? result.value() : null;
   }

   public void apply(final long gameTime, final K key, final DebugSubscription$Update<V> update) {
      if (update.value().isPresent()) {
         this.values.put(key, new ClientDebugSubscriber$ValueWrapper<>(update.value().get(), gameTime + update.subscription().expireAfterTicks()));
      } else {
         this.values.remove(key);
      }
   }

   public void forEach(final BiConsumer<K, V> output) {
      this.values.forEach((k, v) -> output.accept((K)k, v.value()));
   }
}
