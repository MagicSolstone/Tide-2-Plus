package net.minecraft.core.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;

public class DataComponentPatch$Builder {
   private final Reference2ObjectMap<DataComponentType<?>, Object> map = new Reference2ObjectArrayMap();

   private DataComponentPatch$Builder() {
   }

   public <T> DataComponentPatch$Builder set(final DataComponentType<T> type, final T value) {
      this.map.put(type, value);
      return this;
   }

   public <T> DataComponentPatch$Builder remove(final DataComponentType<T> type) {
      this.map.put(type, Removed.INSTANCE);
      return this;
   }

   public <T> DataComponentPatch$Builder set(final TypedDataComponent<T> component) {
      return this.set(component.type(), component.value());
   }

   public <T> DataComponentPatch$Builder set(final Iterable<TypedDataComponent<?>> components) {
      for (TypedDataComponent<?> component : components) {
         this.set(component);
      }

      return this;
   }

   public DataComponentPatch build() {
      return this.map.isEmpty() ? DataComponentPatch.EMPTY : new DataComponentPatch(this.map);
   }
}
