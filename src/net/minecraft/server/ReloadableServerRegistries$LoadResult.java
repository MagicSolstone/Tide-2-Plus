package net.minecraft.server;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.LayeredRegistryAccess;

public record ReloadableServerRegistries$LoadResult(LayeredRegistryAccess<RegistryLayer> layers, HolderLookup$Provider lookupWithUpdatedTags) {
}
