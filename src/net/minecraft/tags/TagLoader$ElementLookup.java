package net.minecraft.tags;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public interface TagLoader$ElementLookup<T> {
   Optional<? extends T> get(Identifier id, boolean required);

   static <T> TagLoader$ElementLookup<? extends Holder<T>> fromFrozenRegistry(final Registry<T> registry) {
      return (id, required) -> registry.get(id);
   }

   static <T> TagLoader$ElementLookup<Holder<T>> fromWritableRegistry(final WritableRegistry<T> registry) {
      return fromGetters(registry.key(), registry.createRegistrationLookup(), registry);
   }

   static <T> TagLoader$ElementLookup<Holder<T>> fromGetters(
      final ResourceKey<? extends Registry<T>> registryKey, final HolderGetter<T> writable, final HolderGetter<T> immutable
   ) {
      return (id, required) -> (required ? writable : immutable).get(ResourceKey.create(registryKey, id));
   }
}
