package net.minecraft.world.attribute;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

public final class EnvironmentAttributeMap {
   public static final EnvironmentAttributeMap EMPTY = new EnvironmentAttributeMap(Map.of());
   public static final Codec<EnvironmentAttributeMap> CODEC = Codec.lazyInitialized(
      () -> Codec.dispatchedMap(EnvironmentAttributes.CODEC, Util.memoize(EnvironmentAttributeMap$Entry::createCodec))
         .xmap(EnvironmentAttributeMap::new, v -> v.entries)
   );
   public static final Codec<EnvironmentAttributeMap> NETWORK_CODEC = CODEC.xmap(
      EnvironmentAttributeMap::filterSyncable, EnvironmentAttributeMap::filterSyncable
   );
   public static final Codec<EnvironmentAttributeMap> CODEC_ONLY_POSITIONAL = CODEC.validate(
      map -> {
         List<EnvironmentAttribute<?>> illegalAttributes = map.keySet().stream().filter(attribute -> !attribute.isPositional()).toList();
         return !illegalAttributes.isEmpty()
            ? DataResult.error(() -> "The following attributes cannot be positional: " + illegalAttributes)
            : DataResult.success(map);
      }
   );
   private final Map<EnvironmentAttribute<?>, EnvironmentAttributeMap$Entry<?, ?>> entries;

   private static EnvironmentAttributeMap filterSyncable(final EnvironmentAttributeMap attributes) {
      return new EnvironmentAttributeMap(Map.copyOf(Maps.filterKeys(attributes.entries, EnvironmentAttribute::isSyncable)));
   }

   private EnvironmentAttributeMap(final Map<EnvironmentAttribute<?>, EnvironmentAttributeMap$Entry<?, ?>> entries) {
      this.entries = entries;
   }

   public static EnvironmentAttributeMap$Builder builder() {
      return new EnvironmentAttributeMap$Builder();
   }

   @Nullable
   public <Value> EnvironmentAttributeMap$Entry<Value, ?> get(final EnvironmentAttribute<Value> attribute) {
      return (EnvironmentAttributeMap$Entry<Value, ?>)this.entries.get(attribute);
   }

   public <Value> Value applyModifier(final EnvironmentAttribute<Value> attribute, final Value baseValue) {
      EnvironmentAttributeMap$Entry<Value, ?> entry = this.get(attribute);
      return entry != null ? entry.applyModifier(baseValue) : baseValue;
   }

   public boolean contains(final EnvironmentAttribute<?> attribute) {
      return this.entries.containsKey(attribute);
   }

   public Set<EnvironmentAttribute<?>> keySet() {
      return this.entries.keySet();
   }

   @Override
   public boolean equals(final Object obj) {
      return obj == this ? true : obj instanceof EnvironmentAttributeMap attributes && this.entries.equals(attributes.entries);
   }

   @Override
   public int hashCode() {
      return this.entries.hashCode();
   }

   @Override
   public String toString() {
      return this.entries.toString();
   }
}
