package net.minecraft.core;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;

public class RegistryAccess$ImmutableRegistryAccess implements RegistryAccess {
   private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries;

   public RegistryAccess$ImmutableRegistryAccess(final List<? extends Registry<?>> registries) {
      this.registries = registries.stream().collect(Collectors.toUnmodifiableMap(Registry::key, v -> v));
   }

   public RegistryAccess$ImmutableRegistryAccess(final Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries) {
      this.registries = Map.copyOf(registries);
   }

   public RegistryAccess$ImmutableRegistryAccess(final Stream<RegistryAccess$RegistryEntry<?>> entries) {
      this.registries = entries.collect(ImmutableMap.toImmutableMap(RegistryAccess$RegistryEntry::key, RegistryAccess$RegistryEntry::value));
   }

   @Override
   public <E> Optional<Registry<E>> lookup(final ResourceKey<? extends Registry<? extends E>> registryKey) {
      return Optional.ofNullable(this.registries.get(registryKey)).map(r -> (Registry<E>)r);
   }

   @Override
   public Stream<RegistryAccess$RegistryEntry<?>> registries() {
      return this.registries.entrySet().stream().map(RegistryAccess$RegistryEntry::fromMapEntry);
   }
}
