package net.minecraft.core;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

public interface HolderLookup$Provider extends HolderGetter$Provider {
   Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys();

   default Stream<HolderLookup$RegistryLookup<?>> listRegistries() {
      return this.listRegistryKeys().map(this::lookupOrThrow);
   }

   @Override
   <T> Optional<? extends HolderLookup$RegistryLookup<T>> lookup(final ResourceKey<? extends Registry<? extends T>> key);

   default <T> HolderLookup$RegistryLookup<T> lookupOrThrow(final ResourceKey<? extends Registry<? extends T>> key) {
      return this.lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + key.identifier() + " not found"));
   }

   default <V> RegistryOps<V> createSerializationContext(final DynamicOps<V> parent) {
      return RegistryOps.create(parent, this);
   }

   static HolderLookup$Provider create(final Stream<HolderLookup$RegistryLookup<?>> lookups) {
      Map<ResourceKey<? extends Registry<?>>, HolderLookup$RegistryLookup<?>> map = lookups.collect(
         Collectors.toUnmodifiableMap(HolderLookup$RegistryLookup::key, e -> e)
      );
      return new HolderLookup$Provider$1(map);
   }

   default Lifecycle allRegistriesLifecycle() {
      return this.listRegistries().map(HolderLookup$RegistryLookup::registryLifecycle).reduce(Lifecycle.stable(), Lifecycle::add);
   }
}
