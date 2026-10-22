package net.minecraft.client.multiplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization$PackedRegistryEntry;
import net.minecraft.resources.ResourceKey;

@Environment(EnvType.CLIENT)
class RegistryDataCollector$ContentsCollector {
   private final Map<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization$PackedRegistryEntry>> elements = new HashMap<>();

   private RegistryDataCollector$ContentsCollector() {
   }

   public void append(final ResourceKey<? extends Registry<?>> registry, final List<RegistrySynchronization$PackedRegistryEntry> elementData) {
      this.elements.computeIfAbsent(registry, ignore -> new ArrayList<>()).addAll(elementData);
   }
}
