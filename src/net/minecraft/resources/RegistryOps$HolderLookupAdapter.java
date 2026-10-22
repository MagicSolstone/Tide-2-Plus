package net.minecraft.resources;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.Registry;

final class RegistryOps$HolderLookupAdapter implements RegistryOps$RegistryInfoLookup {
   private final HolderLookup$Provider lookupProvider;
   private final Map<ResourceKey<? extends Registry<?>>, Optional<? extends HolderGetter<?>>> lookups = new ConcurrentHashMap<>();

   public RegistryOps$HolderLookupAdapter(final HolderLookup$Provider lookupProvider) {
      this.lookupProvider = lookupProvider;
   }

   @Override
   public <E> Optional<HolderGetter<E>> lookup(final ResourceKey<? extends Registry<? extends E>> registryKey) {
      return (Optional<HolderGetter<E>>)this.lookups.computeIfAbsent(registryKey, this.lookupProvider::lookup);
   }

   @Override
   public boolean equals(final Object obj) {
      return this == obj ? true : obj instanceof RegistryOps$HolderLookupAdapter adapter && this.lookupProvider.equals(adapter.lookupProvider);
   }

   @Override
   public int hashCode() {
      return this.lookupProvider.hashCode();
   }
}
