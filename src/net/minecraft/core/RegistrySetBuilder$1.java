package net.minecraft.core;

import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;

class RegistrySetBuilder$1 implements RegistrySetBuilder$RegistryStub {
   RegistrySetBuilder$1(final ResourceKey val$key) {
      this.val$key = val$keyx;
   }

   @Override
   public Stream<ResourceKey<? extends Registry<?>>> requiredRegistries() {
      return Stream.of(this.val$key);
   }

   @Override
   public void apply(final RegistrySetBuilder$BuildState state) {
   }
}
