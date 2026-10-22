package net.minecraft.core.component;

import com.google.common.collect.Sets;
import java.util.Set;
import org.jspecify.annotations.Nullable;

class DataComponentMap$2 implements DataComponentMap {
   DataComponentMap$2(final DataComponentMap val$overrides, final DataComponentMap val$prototype) {
      this.val$overrides = val$overridesx;
      this.val$prototype = val$prototypex;
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      T value = this.val$overrides.get(type);
      return value != null ? value : this.val$prototype.get(type);
   }

   @Override
   public Set<DataComponentType<?>> keySet() {
      return Sets.union(this.val$prototype.keySet(), this.val$overrides.keySet());
   }
}
