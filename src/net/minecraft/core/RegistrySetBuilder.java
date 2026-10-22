package net.minecraft.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.registries.EmptyTagLookupWrapper;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.PatchedRegistry;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.resources.ResourceKey;

public class RegistrySetBuilder {
   private final List<RegistrySetBuilder$RegistryStub> entries = new ArrayList<>();

   private static RegistrySetBuilder$RegistryStub placeholderStub(final ResourceKey<? extends Registry<?>> key) {
      return new RegistrySetBuilder$1(key);
   }

   public <T> RegistrySetBuilder add(final ResourceKey<? extends Registry<T>> key, final SingleRegistryBootstrap<T> bootstrap) {
      this.entries.add(new RegistrySetBuilder$2(this, key, bootstrap));
      return this;
   }

   public RegistrySetBuilder add(final MultiRegistryBootstrap bootstrap) {
      this.entries.add(new RegistrySetBuilder$3(this, bootstrap));
      return this;
   }

   private static HolderLookup$Provider buildProviderWithContext(
      final HolderLookup$Provider context, final Stream<? extends HolderLookup$RegistryLookup<?>> newRegistries
   ) {
      Map<ResourceKey<? extends Registry<?>>, HolderLookup$RegistryLookup<?>> lookups = new HashMap<>();
      context.listRegistries()
         .forEach(contextRegistry -> lookups.put(contextRegistry.key(), EmptyTagLookupWrapper.wrap((HolderLookup$RegistryLookup<?>)contextRegistry)));
      newRegistries.forEach(newRegistry -> lookups.put(newRegistry.key(), EmptyTagLookupWrapper.wrap((HolderLookup$RegistryLookup<?>)newRegistry)));
      return HolderLookup$Provider.create(lookups.values().stream());
   }

   public HolderLookup$Provider build(final HolderLookup$Provider context) {
      RegistrySetBuilder$BuildState state = RegistrySetBuilder$BuildState.createAndApply(context, this.entries);
      List<HolderLookup$RegistryLookup<?>> bootstrappedRegistries = new ArrayList<>(state.bootstrappedRegistries.size());

      for (RegistrySetBuilder$BootstrappedRegistryState<?> newRegistry : state.bootstrappedRegistries.values()) {
         newRegistry.bindHolders();
         newRegistry.freeze();
         newRegistry.errorOnMissingHolders(state);
         bootstrappedRegistries.add(newRegistry.registry());
      }

      state.throwOnError();
      return buildProviderWithContext(context, bootstrappedRegistries.stream());
   }

   private static Set<ResourceKey<? extends Registry<?>>> findRegistriesMissingFromPatch(
      final HolderLookup$Provider contextRegistries, final HolderLookup$Provider baseRegistries, final List<RegistrySetBuilder$RegistryStub> entries
   ) {
      Set<? extends ResourceKey<? extends Registry<?>>> existingKeys = Stream.concat(newRegistryKeys(entries.stream()), contextRegistries.listRegistryKeys())
         .collect(Collectors.toSet());
      return baseRegistries.listRegistryKeys().filter(e -> !existingKeys.contains(e)).collect(Collectors.toSet());
   }

   public RegistrySetBuilder$PatchedRegistries buildPatch(
      final HolderLookup$Provider context, final HolderLookup$Provider fallbackProvider, final Cloner$Factory clonerFactory
   ) {
      Set<ResourceKey<? extends Registry<?>>> missingFromPatch = findRegistriesMissingFromPatch(context, fallbackProvider, this.entries);
      List<RegistrySetBuilder$RegistryStub> expandedEntries = Stream.concat(
            this.entries.stream(), missingFromPatch.stream().map(RegistrySetBuilder::placeholderStub)
         )
         .toList();
      RegistrySetBuilder$BuildState state = RegistrySetBuilder$BuildState.createAndApply(context, expandedEntries);
      List<HolderLookup$RegistryLookup<?>> bootstrappedRegistries = new ArrayList<>(state.bootstrappedRegistries.size());

      for (RegistrySetBuilder$BootstrappedRegistryState<?> newRegistry : state.bootstrappedRegistries.values()) {
         newRegistry.bindHolders();
         newRegistry.validatePatchHolders(state, fallbackProvider);
         newRegistry.freeze();
         bootstrappedRegistries.add(newRegistry.registry());
      }

      HolderLookup$Provider patchOnlyRegistries = buildProviderWithContext(context, bootstrappedRegistries.stream());
      state.throwOnError();
      HolderLookup$Provider fullPatchedRegistries = EmptyTagLookupWrapper.wrap(
         PatchedRegistry.applyPatches(context, fallbackProvider, patchOnlyRegistries, clonerFactory, state.bootstrappedRegistries.keySet())
      );
      return new RegistrySetBuilder$PatchedRegistries(fullPatchedRegistries, patchOnlyRegistries);
   }

   private static <T> ResourceKey<? extends Registry<T>> eyerollCast(final ResourceKey<? extends Registry<? extends T>> registryKey) {
      return (ResourceKey<? extends Registry<T>>)registryKey;
   }

   private static Stream<ResourceKey<? extends Registry<?>>> newRegistryKeys(final Stream<RegistrySetBuilder$RegistryStub> entries) {
      return entries.flatMap(RegistrySetBuilder$RegistryStub::requiredRegistries).distinct();
   }
}
