package net.minecraft.commands;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;

class CommandBuildContext$1 implements CommandBuildContext {
   CommandBuildContext$1(final HolderLookup$Provider val$access, final FeatureFlagSet val$enabledFeatures) {
      this.val$access = val$accessx;
      this.val$enabledFeatures = val$enabledFeaturesx;
   }

   @Override
   public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
      return this.val$access.listRegistryKeys();
   }

   @Override
   public <T> Optional<HolderLookup$RegistryLookup<T>> lookup(final ResourceKey<? extends Registry<? extends T>> key) {
      return this.val$access.lookup(key).map(lookup -> lookup.filterFeatures(enabledFeatures));
   }

   @Override
   public FeatureFlagSet enabledFeatures() {
      return this.val$enabledFeatures;
   }
}
