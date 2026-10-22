package net.minecraft.core;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;

class HolderLookup$Provider$1 implements HolderLookup$Provider {
   HolderLookup$Provider$1(final Map val$map) {
      this.val$map = val$mapx;
   }

   @Override
   public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
      return this.val$map.keySet().stream();
   }

   @Override
   public <T> Optional<HolderLookup$RegistryLookup<T>> lookup(final ResourceKey<? extends Registry<? extends T>> key) {
      return Optional.ofNullable((HolderLookup$RegistryLookup<T>)this.val$map.get(key));
   }
}
