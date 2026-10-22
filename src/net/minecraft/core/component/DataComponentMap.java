package net.minecraft.core.component;

import com.google.common.collect.Iterators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface DataComponentMap extends Iterable<TypedDataComponent<?>>, DataComponentGetter {
   DataComponentMap EMPTY = new DataComponentMap$1();
   Codec<DataComponentMap> CODEC = makeCodecFromMap(DataComponentType.VALUE_MAP_CODEC);

   static Codec<DataComponentMap> makeCodec(final Codec<DataComponentType<?>> componentTypeCodec) {
      return makeCodecFromMap(Codec.dispatchedMap(componentTypeCodec, DataComponentType::codecOrThrow));
   }

   static Codec<DataComponentMap> makeCodecFromMap(final Codec<Map<DataComponentType<?>, Object>> mapCodec) {
      return mapCodec.flatComapMap(DataComponentMap$Builder::buildFromMapTrusted, components -> {
         int size = components.size();
         if (size == 0) {
            return DataResult.success(Reference2ObjectMaps.emptyMap());
         } else {
            Reference2ObjectMap<DataComponentType<?>, Object> map = new Reference2ObjectArrayMap(size);

            for (TypedDataComponent<?> entry : components) {
               if (!entry.type().isTransient()) {
                  map.put(entry.type(), entry.value());
               }
            }

            return DataResult.success(map);
         }
      });
   }

   static DataComponentMap composite(final DataComponentMap prototype, final DataComponentMap overrides) {
      return new DataComponentMap$2(overrides, prototype);
   }

   static DataComponentMap$Builder builder() {
      return new DataComponentMap$Builder();
   }

   Set<DataComponentType<?>> keySet();

   default boolean has(final DataComponentType<?> type) {
      return this.get(type) != null;
   }

   @Override
   default Iterator<TypedDataComponent<?>> iterator() {
      return Iterators.transform(this.keySet().iterator(), type -> Objects.requireNonNull(this.getTyped(type)));
   }

   default Stream<TypedDataComponent<?>> stream() {
      return StreamSupport.stream(Spliterators.spliterator(this.iterator(), (long)this.size(), 1345), false);
   }

   default int size() {
      return this.keySet().size();
   }

   default boolean isEmpty() {
      return this.size() == 0;
   }

   default DataComponentMap filter(final Predicate<DataComponentType<?>> predicate) {
      return new DataComponentMap$3(this, predicate);
   }
}
