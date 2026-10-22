package net.minecraft.core.registries;

import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface MultiRegistryBootstrap {
   Set<ResourceKey<? extends Registry<?>>> requestedRegistries();

   void run(MultiRegistryBootstrap$BootstrapGetter registries);
}
