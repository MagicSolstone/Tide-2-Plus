package net.minecraft.server.dedicated;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

record DedicatedServerProperties$WorldDimensionData(JsonObject generatorSettings, String levelType) {
   private static final Map<String, ResourceKey<WorldPreset>> LEGACY_PRESET_NAMES = Map.of(
      "default", WorldPresets.NORMAL, "largebiomes", WorldPresets.LARGE_BIOMES
   );

   public WorldDimensions create(final HolderLookup$Provider registries) {
      HolderLookup<WorldPreset> worldPresets = registries.lookupOrThrow(Registries.WORLD_PRESET);
      Holder$Reference<WorldPreset> defaultHolder = worldPresets.get(WorldPresets.NORMAL)
         .or(() -> worldPresets.listElements().findAny())
         .orElseThrow(() -> new IllegalStateException("Invalid datapack contents: can't find default preset"));
      Holder<WorldPreset> worldPreset = Optional.ofNullable(Identifier.tryParse(this.levelType))
         .map(id -> ResourceKey.create(Registries.WORLD_PRESET, id))
         .or(() -> Optional.ofNullable(LEGACY_PRESET_NAMES.get(this.levelType)))
         .flatMap(worldPresets::get)
         .orElseGet(() -> {
            DedicatedServerProperties.LOGGER.warn("Failed to parse level-type {}, defaulting to {}", this.levelType, defaultHolder.key().identifier());
            return defaultHolder;
         });
      WorldDimensions worldDimensions = worldPreset.value().createWorldDimensions();
      if (worldPreset.is(WorldPresets.FLAT)) {
         RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
         Optional<FlatLevelGeneratorSettings> parsedSettings = FlatLevelGeneratorSettings.CODEC
            .parse(new Dynamic(ops, this.generatorSettings()))
            .resultOrPartial(DedicatedServerProperties.LOGGER::error);
         if (parsedSettings.isPresent()) {
            return worldDimensions.replaceOverworldGenerator(registries, new FlatLevelSource(parsedSettings.get()));
         }
      }

      return worldDimensions;
   }
}
