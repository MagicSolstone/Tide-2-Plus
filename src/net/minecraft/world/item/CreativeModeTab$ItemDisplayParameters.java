package net.minecraft.world.item;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.world.flag.FeatureFlagSet;

public record CreativeModeTab$ItemDisplayParameters(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup$Provider holders) {
   public boolean needsUpdate(final FeatureFlagSet enabledFeatures, final boolean hasPermissions, final HolderLookup$Provider holders) {
      return !this.enabledFeatures.equals(enabledFeatures) || this.hasPermissions != hasPermissions || this.holders != holders;
   }
}
