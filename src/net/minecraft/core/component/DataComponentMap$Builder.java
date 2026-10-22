package net.minecraft.core.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;

public class DataComponentMap$Builder {
   private final Reference2ObjectMap<DataComponentType<?>, Object> map = new Reference2ObjectArrayMap();
   private Consumer<DataComponentMap> validator = components -> {};

   private DataComponentMap$Builder() {
   }

   public <T> DataComponentMap$Builder set(final DataComponentType<T> type, @Nullable final T value) {
      this.setUnchecked(type, value);
      return this;
   }

   <T> void setUnchecked(final DataComponentType<T> type, @Nullable final Object value) {
      if (value != null) {
         this.map.put(type, value);
      } else {
         this.map.remove(type);
      }
   }

   public DataComponentMap$Builder addAll(final DataComponentMap map) {
      for (TypedDataComponent<?> entry : map) {
         this.map.put(entry.type(), entry.value());
      }

      return this;
   }

   public DataComponentMap$Builder addValidator(final Consumer<DataComponentMap> newValidator) {
      this.validator = this.validator.andThen(newValidator);
      return this;
   }

   public DataComponentMap build() {
      DataComponentMap result = buildFromMapTrusted(this.map);
      this.validator.accept(result);
      return result;
   }

   private static DataComponentMap buildFromMapTrusted(final Map<DataComponentType<?>, Object> map) {
      if (map.isEmpty()) {
         return DataComponentMap.EMPTY;
      } else {
         return map.size() < 8
            ? new DataComponentMap$Builder$SimpleMap(new Reference2ObjectArrayMap(map))
            : new DataComponentMap$Builder$SimpleMap(new Reference2ObjectOpenHashMap(map));
      }
   }
}
