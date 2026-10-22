package net.minecraft.client.multiplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagNetworkSerialization$NetworkPayload;

@Environment(EnvType.CLIENT)
class RegistryDataCollector$TagCollector {
   private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization$NetworkPayload> tags = new HashMap<>();

   private RegistryDataCollector$TagCollector() {
   }

   public void append(final ResourceKey<? extends Registry<?>> registry, final TagNetworkSerialization$NetworkPayload tagData) {
      this.tags.put(registry, tagData);
   }

   public void forEach(final BiConsumer<? super ResourceKey<? extends Registry<?>>, ? super TagNetworkSerialization$NetworkPayload> action) {
      this.tags.forEach(action);
   }
}
