package net.minecraft.core;

import java.util.Map.Entry;
import net.minecraft.resources.ResourceKey;

public record RegistryAccess$RegistryEntry<T>(ResourceKey<? extends Registry<T>> key, Registry<T> value) {
   private static <T, R extends Registry<? extends T>> RegistryAccess$RegistryEntry<T> fromMapEntry(
      final Entry<? extends ResourceKey<? extends Registry<?>>, R> e
   ) {
      return fromUntyped((ResourceKey<? extends Registry<?>>)e.getKey(), e.getValue());
   }

   private static <T> RegistryAccess$RegistryEntry<T> fromUntyped(final ResourceKey<? extends Registry<?>> key, final Registry<?> value) {
      return new RegistryAccess$RegistryEntry<>((ResourceKey<? extends Registry<T>>)key, (Registry<T>)value);
   }

   private RegistryAccess$RegistryEntry<T> freeze() {
      return new RegistryAccess$RegistryEntry<>(this.key, this.value.freeze());
   }
}
