package net.minecraft.core.component;

import net.minecraft.core.HolderLookup$Provider;

@FunctionalInterface
public interface DataComponentInitializers$SingleComponentInitializer<C> {
   C create(HolderLookup$Provider context);

   default <T> DataComponentInitializers$Initializer<T> asInitializer(final DataComponentType<C> type) {
      return (components, context, key) -> components.set(type, this.create(context));
   }
}
