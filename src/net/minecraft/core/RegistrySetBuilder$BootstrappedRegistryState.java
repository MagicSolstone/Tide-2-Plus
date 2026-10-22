package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.BootstrapRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

record RegistrySetBuilder$BootstrappedRegistryState<T>(BootstrapRegistry<T> registry, Map<ResourceKey<T>, T> registeredValues) {
   public static <T> RegistrySetBuilder$BootstrappedRegistryState<T> create(final ResourceKey<? extends Registry<T>> key, final Lifecycle lifecycle) {
      BootstrapRegistry<T> newRegistry = new BootstrapRegistry<>(key, lifecycle);
      return new RegistrySetBuilder$BootstrappedRegistryState<>(newRegistry, new HashMap<>());
   }

   public BootstrapContext<T> createBootstrapContext(final RegistrySetBuilder$BuildState state) {
      return new RegistrySetBuilder$BootstrappedRegistryState$1(this, state);
   }

   public void bindHolders() {
      this.registeredValues.forEach((key, value) -> this.registry.getOrThrow((ResourceKey<T>)key).bindValue((T)value));
   }

   public void freeze() {
      this.registry.freeze();
   }

   public void errorOnMissingHolders(final RegistrySetBuilder$BuildState state) {
      this.registry.listElements().forEach(element -> {
         if (!element.isBound()) {
            state.errors().add(new IllegalStateException("No value registered for key " + element.key().identifier()));
         }
      });
   }

   public void validatePatchHolders(final RegistrySetBuilder$BuildState state, final HolderLookup$Provider fallback) {
      HolderLookup<T> baseRegistry = fallback.lookupOrThrow(this.registry.key());
      this.registry.removeIf(element -> {
         if (element.isBound()) {
            return false;
         } else {
            if (baseRegistry.get(element.key()).isEmpty()) {
               state.errors().add(new IllegalStateException("Value " + element.key().identifier() + " referenced by patched element is not present in base"));
            }

            return true;
         }
      });
   }
}
