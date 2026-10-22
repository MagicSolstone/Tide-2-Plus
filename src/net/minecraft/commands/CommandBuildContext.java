package net.minecraft.commands;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.world.flag.FeatureFlagSet;

public interface CommandBuildContext extends HolderLookup$Provider {
   static CommandBuildContext simple(final HolderLookup$Provider access, final FeatureFlagSet enabledFeatures) {
      return new CommandBuildContext$1(access, enabledFeatures);
   }

   FeatureFlagSet enabledFeatures();
}
