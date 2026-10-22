package net.minecraft.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Lifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.registries.BootstrapRegistry;
import net.minecraft.core.registries.EmptyTagLookupWrapper;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

record RegistrySetBuilder$BuildState(
   HolderLookup$Provider contextRegistries,
   HolderLookup$Provider allRegistries,
   Map<ResourceKey<? extends Registry<?>>, RegistrySetBuilder$BootstrappedRegistryState<?>> bootstrappedRegistries,
   List<RuntimeException> errors
) {
   public static RegistrySetBuilder$BuildState createAndApply(final HolderLookup$Provider context, final List<RegistrySetBuilder$RegistryStub> entries) {
      RegistrySetBuilder$BuildState state = create(context, entries);
      entries.forEach(e -> e.apply(state));
      return state;
   }

   private static RegistrySetBuilder$BuildState create(final HolderLookup$Provider context, final List<RegistrySetBuilder$RegistryStub> entries) {
      List<RuntimeException> errors = new ArrayList<>();
      Builder<ResourceKey<? extends Registry<?>>, HolderLookup$RegistryLookup<?>> allRegistries = ImmutableMap.builder();
      Builder<ResourceKey<? extends Registry<?>>, RegistrySetBuilder$BootstrappedRegistryState<?>> bootstrappedRegistries = ImmutableMap.builder();
      context.listRegistries()
         .forEach(contextRegistry -> allRegistries.put(contextRegistry.key(), EmptyTagLookupWrapper.wrap((HolderLookup$RegistryLookup<?>)contextRegistry)));
      RegistrySetBuilder.newRegistryKeys(entries.stream())
         .forEach(
            newRegistryKey -> {
               RegistrySetBuilder$BootstrappedRegistryState<?> newRegistryEntry = RegistrySetBuilder$BootstrappedRegistryState.create(
                  RegistrySetBuilder.eyerollCast((ResourceKey<? extends Registry<?>>)newRegistryKey), Lifecycle.stable()
               );
               BootstrapRegistry<?> newRegistry = newRegistryEntry.registry();
               bootstrappedRegistries.put(newRegistry.key(), newRegistryEntry);
               allRegistries.put(newRegistry.key(), newRegistry);
            }
         );
      return new RegistrySetBuilder$BuildState(
         context, HolderLookup$Provider.create(allRegistries.build().values().stream()), bootstrappedRegistries.build(), errors
      );
   }

   public void throwOnError() {
      if (!this.errors.isEmpty()) {
         IllegalStateException result = new IllegalStateException("Errors during registry creation");

         for (RuntimeException error : this.errors) {
            result.addSuppressed(error);
         }

         throw result;
      }
   }

   public <T> BootstrapContext<T> createBootstrap(final ResourceKey<? extends Registry<T>> key) {
      RegistrySetBuilder$BootstrappedRegistryState<T> targetRegistry = Objects.requireNonNull(
         (RegistrySetBuilder$BootstrappedRegistryState<T>)this.bootstrappedRegistries.get(key), () -> "No registry named " + key.identifier()
      );
      return targetRegistry.createBootstrapContext(this);
   }
}
