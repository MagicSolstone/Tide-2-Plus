package net.minecraft.core.component;

import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

record DataComponentInitializers$PendingComponentBuilders<T>(
   ResourceKey<? extends Registry<T>> registryKey, Map<ResourceKey<T>, DataComponentMap$Builder> builders
) {
}
