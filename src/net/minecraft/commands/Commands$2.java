package net.minecraft.commands;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.HolderLookup$RegistryLookup$Delegate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

class Commands$2 implements CommandBuildContext {
   Commands$2(final HolderLookup$Provider val$registries) {
      this.val$registries = val$registriesx;
   }

   @Override
   public FeatureFlagSet enabledFeatures() {
      return FeatureFlags.REGISTRY.allFlags();
   }

   @Override
   public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
      return this.val$registries.listRegistryKeys();
   }

   @Override
   public <T> Optional<HolderLookup$RegistryLookup<T>> lookup(final ResourceKey<? extends Registry<? extends T>> key) {
      return this.val$registries.lookup(key).map(this::createLookup);
   }

   private <T> HolderLookup$RegistryLookup$Delegate<T> createLookup(final HolderLookup$RegistryLookup<T> original) {
      return new Commands$2$1(this, original);
   }
}
