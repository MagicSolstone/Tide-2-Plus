package net.minecraft.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ExtraCodecs$LateBoundIdMapper<I, V> {
   private final BiMap<I, V> idToValue = HashBiMap.create();

   public Codec<V> codec(final Codec<I> idCodec) {
      BiMap<V, I> valueToId = this.idToValue.inverse();
      return ExtraCodecs.idResolverCodec(idCodec, this.idToValue::get, valueToId::get);
   }

   public ExtraCodecs$LateBoundIdMapper<I, V> put(final I id, final V value) {
      Objects.requireNonNull(value, () -> "Value for " + id + " is null");
      this.idToValue.put(id, value);
      return this;
   }

   public Set<V> values() {
      return Collections.unmodifiableSet(this.idToValue.values());
   }
}
