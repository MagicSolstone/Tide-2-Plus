package net.minecraft.core;

import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.resources.ResourceKey;

class RegistrySetBuilder$3 implements RegistrySetBuilder$RegistryStub {
   RegistrySetBuilder$3(final RegistrySetBuilder this$0, final MultiRegistryBootstrap val$bootstrap) {
      this.val$bootstrap = val$bootstrapx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public Stream<ResourceKey<? extends Registry<?>>> requiredRegistries() {
      return this.val$bootstrap.requestedRegistries().stream();
   }

   @Override
   public void apply(final RegistrySetBuilder$BuildState state) {
      this.val$bootstrap.run(state::createBootstrap);
   }
}
