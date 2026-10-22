package net.minecraft.server;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.packs.resources.CloseableResourceManager;

@FunctionalInterface
public interface WorldLoader$ResultFactory<D, R> {
   R create(CloseableResourceManager resources, ReloadableServerResources managers, LayeredRegistryAccess<RegistryLayer> registries, D cookie);
}
