package net.minecraft.core.component;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.resources.ResourceKey;

@FunctionalInterface
public interface DataComponentInitializers$Initializer<T> {
   void run(DataComponentMap$Builder components, HolderLookup$Provider context, ResourceKey<T> key);

   default DataComponentInitializers$Initializer<T> andThen(final DataComponentInitializers$Initializer<T> other) {
      return (components, context, key) -> {
         this.run(components, context, key);
         other.run(components, context, key);
      };
   }

   default <C> DataComponentInitializers$Initializer<T> add(final DataComponentType<C> type, final C value) {
      return this.andThen((components, context, key) -> components.set(type, value));
   }
}
