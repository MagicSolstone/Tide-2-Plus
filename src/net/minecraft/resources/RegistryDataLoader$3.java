package net.minecraft.resources;

import java.util.Map;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;

class RegistryDataLoader$3 implements RegistryOps$RegistryInfoLookup {
   RegistryDataLoader$3(final Map val$result) {
      this.val$result = val$resultx;
   }

   @Override
   public <T> Optional<HolderGetter<T>> lookup(final ResourceKey<? extends Registry<? extends T>> key) {
      return Optional.ofNullable((HolderGetter<T>)this.val$result.get(key));
   }
}
