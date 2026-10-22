package net.minecraft.core;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;

class RegistryAccess$1 implements RegistryAccess$Frozen {
   RegistryAccess$1(final Registry val$registries) {
      this.val$registries = val$registriesx;
   }

   @Override
   public <T> Optional<Registry<T>> lookup(final ResourceKey<? extends Registry<? extends T>> registryKey) {
      Registry<Registry<T>> registry = this.val$registries;
      return registry.getOptional((ResourceKey<Registry<T>>)registryKey);
   }

   @Override
   public Stream<RegistryAccess$RegistryEntry<?>> registries() {
      return this.val$registries.entrySet().stream().map(RegistryAccess$RegistryEntry::fromMapEntry);
   }

   @Override
   public RegistryAccess$Frozen freeze() {
      return this;
   }
}
