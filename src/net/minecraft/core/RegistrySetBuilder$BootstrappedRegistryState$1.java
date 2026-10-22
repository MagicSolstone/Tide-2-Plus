package net.minecraft.core;

import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

class RegistrySetBuilder$BootstrappedRegistryState$1 implements BootstrapContext<T> {
   RegistrySetBuilder$BootstrappedRegistryState$1(final RegistrySetBuilder$BootstrappedRegistryState this$0, final RegistrySetBuilder$BuildState val$state) {
      this.val$state = val$statex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public Holder$Reference<T> register(final ResourceKey<T> key, final T value) {
      T previousValue = (T)this.this$0.registeredValues.put(key, value);
      if (previousValue != null) {
         this.val$state.errors.add(new IllegalStateException("Duplicate registration for " + key + ", new=" + value + ", old=" + previousValue));
      }

      return this.this$0.registry.getOrThrow(key);
   }

   @Override
   public <S> HolderGetter<S> lookup(final ResourceKey<? extends Registry<? extends S>> key) {
      return this.val$state.allRegistries.lookupOrThrow(key);
   }

   @Override
   public <S> Stream<Holder$Reference<S>> listContextElements(final ResourceKey<? extends Registry<? extends S>> key) {
      return this.val$state.contextRegistries.lookupOrThrow(key).listElements();
   }
}
