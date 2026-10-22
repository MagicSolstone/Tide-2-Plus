package net.minecraft.core.component;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

public final class DataComponentPatch {
   public static final DataComponentPatch EMPTY = new DataComponentPatch(Reference2ObjectMaps.emptyMap());
   public static final Codec<DataComponentPatch> CODEC = Codec.dispatchedMap(DataComponentPatch$PatchKey.CODEC, DataComponentPatch$PatchKey::valueCodec)
      .xmap(data -> {
         if (data.isEmpty()) {
            return EMPTY;
         } else {
            Reference2ObjectMap<DataComponentType<?>, Object> map = new Reference2ObjectArrayMap(data.size());

            for (Entry<DataComponentPatch$PatchKey, ?> entry : data.entrySet()) {
               map.put(entry.getKey().type(), entry.getValue());
            }

            return new DataComponentPatch(map);
         }
      }, patch -> {
         Reference2ObjectMap<DataComponentPatch$PatchKey, Object> map = new Reference2ObjectArrayMap(patch.map.size());
         Iterator i$ = Reference2ObjectMaps.fastIterable(patch.map).iterator();

         while (i$.hasNext()) {
            Entry<DataComponentType<?>, Object> entry = (Entry<DataComponentType<?>, Object>)i$.next();
            DataComponentType<?> type = entry.getKey();
            if (!type.isTransient()) {
               Object value = entry.getValue();
               map.put(new DataComponentPatch$PatchKey(type, Removed.isRemoved(value)), value);
            }
         }

         return map;
      });
   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> STREAM_CODEC = createStreamCodec(new DataComponentPatch$1());
   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> DELIMITED_STREAM_CODEC = createStreamCodec(new DataComponentPatch$2());
   private static final String REMOVED_PREFIX = "!";
   final Reference2ObjectMap<DataComponentType<?>, Object> map;

   private static StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> createStreamCodec(final DataComponentPatch$CodecGetter codecGetter) {
      return new DataComponentPatch$3(codecGetter);
   }

   DataComponentPatch(final Reference2ObjectMap<DataComponentType<?>, Object> map) {
      this.map = map;
   }

   public static DataComponentPatch$Builder builder() {
      return new DataComponentPatch$Builder();
   }

   @Nullable
   public <T> T get(final DataComponentGetter prototype, final DataComponentType<? extends T> type) {
      return getFromPatchAndPrototype(this.map, prototype, type);
   }

   static <T> T getFromPatchAndPrototype(
      final Reference2ObjectMap<DataComponentType<?>, Object> patch, final DataComponentGetter prototype, final DataComponentType<? extends T> type
   ) {
      Object value = patch.get(type);
      return (T)(value != null ? Removed.removedToNull(value) : prototype.get(type));
   }

   public int size() {
      return this.map.size();
   }

   public DataComponentPatch forget(final Predicate<DataComponentType<?>> test) {
      if (this.isEmpty()) {
         return EMPTY;
      } else {
         Reference2ObjectMap<DataComponentType<?>, Object> newMap = new Reference2ObjectArrayMap(this.map);
         newMap.keySet().removeIf(test);
         return newMap.isEmpty() ? EMPTY : new DataComponentPatch(newMap);
      }
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public DataComponentPatch$SplitResult split() {
      if (this.isEmpty()) {
         return DataComponentPatch$SplitResult.EMPTY;
      } else {
         DataComponentMap$Builder added = DataComponentMap.builder();
         Set<DataComponentType<?>> removed = Sets.newIdentityHashSet();
         this.map.forEach((type, value) -> {
            if (Removed.isNotRemoved(value)) {
               added.setUnchecked(type, value);
            } else {
               removed.add(type);
            }
         });
         return new DataComponentPatch$SplitResult(added.build(), removed);
      }
   }

   @Override
   public boolean equals(final Object obj) {
      return this == obj ? true : obj instanceof DataComponentPatch patch && this.map.equals(patch.map);
   }

   @Override
   public int hashCode() {
      return this.map.hashCode();
   }

   @Override
   public String toString() {
      return toString(this.map);
   }

   static String toString(final Reference2ObjectMap<DataComponentType<?>, Object> map) {
      StringBuilder builder = new StringBuilder();
      builder.append('{');
      boolean first = true;
      ObjectIterator var3 = Reference2ObjectMaps.fastIterable(map).iterator();

      while (var3.hasNext()) {
         Entry<DataComponentType<?>, Object> entry = (Entry<DataComponentType<?>, Object>)var3.next();
         if (first) {
            first = false;
         } else {
            builder.append(", ");
         }

         Object value = entry.getValue();
         if (Removed.isNotRemoved(value)) {
            builder.append(entry.getKey());
            builder.append("=>");
            builder.append(value);
         } else {
            builder.append("!");
            builder.append(entry.getKey());
         }
      }

      builder.append('}');
      return builder.toString();
   }
}
