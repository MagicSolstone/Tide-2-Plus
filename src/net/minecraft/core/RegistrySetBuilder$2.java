package net.minecraft.core;

import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.resources.ResourceKey;

class RegistrySetBuilder$2 implements RegistrySetBuilder$RegistryStub {
   RegistrySetBuilder$2(final RegistrySetBuilder this$0, final ResourceKey val$key, final SingleRegistryBootstrap val$bootstrap) {
      this.val$key = val$keyx;
      this.val$bootstrap = val$bootstrapx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public Stream<ResourceKey<? extends Registry<?>>> requiredRegistries() {
      return Stream.of(this.val$key);
   }

   @Override
   public void apply(final RegistrySetBuilder$BuildState state) {
      this.val$bootstrap.run(state.createBootstrap(this.val$key));
   }
}
