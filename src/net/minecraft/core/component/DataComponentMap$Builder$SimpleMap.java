package net.minecraft.core.component;

import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import java.util.Iterator;
import java.util.Set;
import org.jspecify.annotations.Nullable;

record DataComponentMap$Builder$SimpleMap(Reference2ObjectMap<DataComponentType<?>, Object> map) implements DataComponentMap {
   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return (T)this.map.get(type);
   }

   @Override
   public boolean has(final DataComponentType<?> type) {
      return this.map.containsKey(type);
   }

   @Override
   public Set<DataComponentType<?>> keySet() {
      return this.map.keySet();
   }

   @Override
   public Iterator<TypedDataComponent<?>> iterator() {
      return Iterators.transform(Reference2ObjectMaps.fastIterator(this.map), TypedDataComponent::fromEntryUnchecked);
   }

   @Override
   public int size() {
      return this.map.size();
   }

   @Override
   public String toString() {
      return this.map.toString();
   }
}
