package net.minecraft.core.registries;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public interface MultiRegistryBootstrap$BootstrapGetter {
   <T> BootstrapContext<T> get(ResourceKey<? extends Registry<T>> key);
}
