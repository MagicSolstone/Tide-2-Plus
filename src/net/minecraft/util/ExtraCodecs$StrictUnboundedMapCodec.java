package net.minecraft.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DataResult.Error;
import com.mojang.serialization.codecs.BaseMapCodec;
import java.util.Map;
import java.util.Optional;

public record ExtraCodecs$StrictUnboundedMapCodec<K, V>(Codec<K> keyCodec, Codec<V> elementCodec) implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
   public <T> DataResult<Map<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
      Builder<K, V> read = ImmutableMap.builder();

      for (Pair<T, T> pair : input.entries().toList()) {
         DataResult<K> k = this.keyCodec().parse(ops, pair.getFirst());
         DataResult<V> v = this.elementCodec().parse(ops, pair.getSecond());
         DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
         Optional<Error<Pair<K, V>>> error = entry.error();
         if (error.isPresent()) {
            String errorMessage = error.get().message();
            return DataResult.error(() -> k.result().isPresent() ? "Map entry '" + k.result().get() + "' : " + errorMessage : errorMessage);
         }

         if (!entry.result().isPresent()) {
            return DataResult.error(() -> "Empty or invalid map contents are not allowed");
         }

         Pair<K, V> kvPair = (Pair<K, V>)entry.result().get();
         read.put(kvPair.getFirst(), kvPair.getSecond());
      }

      Map<K, V> elements = read.build();
      return DataResult.success(elements);
   }

   public <T> DataResult<Pair<Map<K, V>, T>> decode(final DynamicOps<T> ops, final T input) {
      return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> this.decode(ops, map)).map(r -> Pair.of(r, input));
   }

   public <T> DataResult<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final T prefix) {
      return this.encode(input, ops, ops.mapBuilder()).build(prefix);
   }

   @Override
   public String toString() {
      return "StrictUnboundedMapCodec[" + this.keyCodec + " -> " + this.elementCodec + "]";
   }
}
