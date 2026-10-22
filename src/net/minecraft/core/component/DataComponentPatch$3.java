package net.minecraft.core.component;

import it.unimi.dsi.fastutil.objects.ObjectIterable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class DataComponentPatch$3 implements StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> {
   DataComponentPatch$3(final DataComponentPatch$CodecGetter val$codecGetter) {
      this.val$codecGetter = val$codecGetterx;
   }

   public DataComponentPatch decode(final RegistryFriendlyByteBuf input) {
      int positiveCount = input.readVarInt();
      int negativeCount = input.readVarInt();
      if (positiveCount == 0 && negativeCount == 0) {
         return DataComponentPatch.EMPTY;
      } else {
         int expectedSize = positiveCount + negativeCount;
         Reference2ObjectMap<DataComponentType<?>, Object> map = new Reference2ObjectArrayMap(Math.min(expectedSize, 65536));

         for (int i = 0; i < positiveCount; i++) {
            DataComponentType<?> type = DataComponentType.STREAM_CODEC.decode(input);
            Object value = this.val$codecGetter.apply(type).decode(input);
            map.put(type, value);
         }

         for (int i = 0; i < negativeCount; i++) {
            DataComponentType<?> type = DataComponentType.STREAM_CODEC.decode(input);
            map.put(type, Removed.INSTANCE);
         }

         return new DataComponentPatch(map);
      }
   }

   public void encode(final RegistryFriendlyByteBuf output, final DataComponentPatch patch) {
      if (patch.isEmpty()) {
         output.writeVarInt(0);
         output.writeVarInt(0);
      } else {
         ObjectIterable<Entry<DataComponentType<?>, Object>> fastEntries = Reference2ObjectMaps.fastIterable(patch.map);
         int positiveCount = 0;
         int negativeCount = 0;
         ObjectIterator var6 = fastEntries.iterator();

         while (var6.hasNext()) {
            Entry<DataComponentType<?>, Object> entry = (Entry<DataComponentType<?>, Object>)var6.next();
            if (Removed.isNotRemoved(entry.getValue())) {
               positiveCount++;
            } else {
               negativeCount++;
            }
         }

         output.writeVarInt(positiveCount);
         output.writeVarInt(negativeCount);
         var6 = fastEntries.iterator();

         while (var6.hasNext()) {
            Entry<DataComponentType<?>, Object> entry = (Entry<DataComponentType<?>, Object>)var6.next();
            Object value = entry.getValue();
            if (Removed.isNotRemoved(value)) {
               DataComponentType<?> type = (DataComponentType<?>)entry.getKey();
               DataComponentType.STREAM_CODEC.encode(output, type);
               this.encodeComponent(output, type, value);
            }
         }

         var6 = fastEntries.iterator();

         while (var6.hasNext()) {
            Entry<DataComponentType<?>, Object> entry = (Entry<DataComponentType<?>, Object>)var6.next();
            if (Removed.isRemoved(entry.getValue())) {
               DataComponentType<?> type = (DataComponentType<?>)entry.getKey();
               DataComponentType.STREAM_CODEC.encode(output, type);
            }
         }
      }
   }

   private <T> void encodeComponent(final RegistryFriendlyByteBuf output, final DataComponentType<T> type, final Object value) {
      this.val$codecGetter.apply(type).encode(output, (T)value);
   }
}
