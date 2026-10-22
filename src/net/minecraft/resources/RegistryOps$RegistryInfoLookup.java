package net.minecraft.resources;

import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;

public interface RegistryOps$RegistryInfoLookup {
   <T> Optional<HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey);
}
