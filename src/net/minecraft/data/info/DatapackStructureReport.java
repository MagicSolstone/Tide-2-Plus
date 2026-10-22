package net.minecraft.data.info;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput$Target;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

public class DatapackStructureReport implements DataProvider {
   private final PackOutput output;
   private static final DatapackStructureReport$Entry STABLE_DYNAMIC_REGISTRY = new DatapackStructureReport$Entry(true, true, true);
   private static final DatapackStructureReport$Entry UNSTABLE_DYNAMIC_REGISTRY = new DatapackStructureReport$Entry(true, true, false);
   private static final DatapackStructureReport$Entry BUILT_IN_REGISTRY = new DatapackStructureReport$Entry(false, true, true);
   private static final Map<String, DatapackStructureReport$CustomPackEntry> NON_REGISTRY_ENTRIES = Map.of(
      "structure",
      new DatapackStructureReport$CustomPackEntry(DatapackStructureReport$Format.STRUCTURE, new DatapackStructureReport$Entry(true, false, true)),
      "function",
      new DatapackStructureReport$CustomPackEntry(DatapackStructureReport$Format.MCFUNCTION, new DatapackStructureReport$Entry(true, true, true))
   );
   private static final Codec<ResourceKey<? extends Registry<?>>> REGISTRY_KEY_CODEC = Identifier.CODEC
      .xmap(ResourceKey::createRegistryKey, ResourceKey::identifier);

   public DatapackStructureReport(final PackOutput output) {
      this.output = output;
   }

   @Override
   public CompletableFuture<?> run(final CachedOutput cache) {
      DatapackStructureReport$Report report = new DatapackStructureReport$Report(this.listRegistries(), NON_REGISTRY_ENTRIES);
      Path path = this.output.getOutputFolder(PackOutput$Target.REPORTS).resolve("datapack.json");
      return DataProvider.saveStable(cache, (JsonElement)DatapackStructureReport$Report.CODEC.encodeStart(JsonOps.INSTANCE, report).getOrThrow(), path);
   }

   @Override
   public String getName() {
      return "Datapack Structure";
   }

   private void putIfNotPresent(
      final Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport$Entry> output,
      final ResourceKey<? extends Registry<?>> key,
      final DatapackStructureReport$Entry entry
   ) {
      DatapackStructureReport$Entry previous = output.putIfAbsent(key, entry);
      if (previous != null) {
         throw new IllegalStateException("Duplicate entry for key " + key.identifier());
      }
   }

   private Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport$Entry> listRegistries() {
      Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport$Entry> result = new HashMap<>();
      BuiltInRegistries.REGISTRY.forEach(entry -> this.putIfNotPresent(result, entry.key(), BUILT_IN_REGISTRY));
      RegistryDataLoader.WORLD_REGISTRIES.forEach(entry -> this.putIfNotPresent(result, entry.key(), UNSTABLE_DYNAMIC_REGISTRY));
      RegistryDataLoader.DIMENSION_REGISTRIES.forEach(entry -> this.putIfNotPresent(result, entry.key(), UNSTABLE_DYNAMIC_REGISTRY));
      RegistryDataLoader.RELOADABLE_REGISTRIES.forEach(entry -> this.putIfNotPresent(result, entry.key(), STABLE_DYNAMIC_REGISTRY));
      return result;
   }
}
