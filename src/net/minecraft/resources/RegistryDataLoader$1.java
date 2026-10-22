package net.minecraft.resources;

import com.mojang.serialization.Lifecycle;
import java.util.Map;
import net.minecraft.server.packs.resources.ResourceManager;

class RegistryDataLoader$1 implements RegistryDataLoader$LoaderFactory {
   RegistryDataLoader$1(final ResourceManager val$resourceManager) {
      this.val$resourceManager = val$resourceManagerx;
   }

   @Override
   public <T> RegistryLoadTask<T> create(final RegistryDataLoader$RegistryData<T> data, final Map<ResourceKey<?>, Exception> loadingErrors) {
      return new ResourceManagerRegistryLoadTask<>(data, Lifecycle.stable(), loadingErrors, this.val$resourceManager);
   }
}
