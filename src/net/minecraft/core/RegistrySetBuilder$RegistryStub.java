package net.minecraft.core;

import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;

interface RegistrySetBuilder$RegistryStub {
   Stream<ResourceKey<? extends Registry<?>>> requiredRegistries();

   void apply(RegistrySetBuilder$BuildState state);
}
