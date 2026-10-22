package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;

public interface HolderLookup$RegistryLookup<T> extends HolderLookup<T> {
   ResourceKey<? extends Registry<? extends T>> key();

   Lifecycle registryLifecycle();

   default HolderLookup$RegistryLookup<T> filterFeatures(final FeatureFlagSet enabledFeatures) {
      return FeatureElement.FILTERED_REGISTRIES.contains(this.key()) ? this.filterElements(t -> ((FeatureElement)t).isEnabled(enabledFeatures)) : this;
   }

   default HolderLookup$RegistryLookup<T> filterElements(final Predicate<T> filter) {
      return new HolderLookup$RegistryLookup$1(this, filter);
   }
}
