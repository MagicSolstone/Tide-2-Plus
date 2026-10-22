package net.minecraft.resources;

import com.mojang.serialization.Lifecycle;
import java.util.Map;
import net.minecraft.server.packs.resources.ResourceProvider;

class RegistryDataLoader$2 implements RegistryDataLoader$LoaderFactory {
   RegistryDataLoader$2(final Map val$entries, final ResourceProvider val$knownDataSource) {
      this.val$entries = val$entriesx;
      this.val$knownDataSource = val$knownDataSourcex;
   }

   @Override
   public <T> RegistryLoadTask<T> create(final RegistryDataLoader$RegistryData<T> data, final Map<ResourceKey<?>, Exception> loadingErrors) {
      return new NetworkRegistryLoadTask<>(data, Lifecycle.stable(), loadingErrors, this.val$entries, this.val$knownDataSource);
   }
}
