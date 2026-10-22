package net.minecraft.core.component;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.jspecify.annotations.Nullable;

class DataComponentMap$1 implements DataComponentMap {
   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      return null;
   }

   @Override
   public Set<DataComponentType<?>> keySet() {
      return Set.of();
   }

   @Override
   public Iterator<TypedDataComponent<?>> iterator() {
      return Collections.emptyIterator();
   }
}
