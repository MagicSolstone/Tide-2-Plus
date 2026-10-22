package net.minecraft.util;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps$RegistryInfoLookup;
import net.minecraft.resources.ResourceKey;

class PlaceholderLookupProvider$1 implements RegistryOps$RegistryInfoLookup {
   PlaceholderLookupProvider$1(final PlaceholderLookupProvider this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> Optional<HolderGetter<T>> lookup(final ResourceKey<? extends Registry<? extends T>> registryKey) {
      Optional<HolderGetter<T>> result = this.this$0.context.lookup(registryKey).map(e -> (HolderGetter<T>)e);
      return result.or(() -> Optional.of(this.this$0.lookup.castAsLookup()));
   }
}
