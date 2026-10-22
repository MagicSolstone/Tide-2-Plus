package net.minecraft.resources;

import java.util.Map;

@FunctionalInterface
interface RegistryDataLoader$LoaderFactory {
   <T> RegistryLoadTask<T> create(RegistryDataLoader$RegistryData<T> data, Map<ResourceKey<?>, Exception> loadingErrors);
}
