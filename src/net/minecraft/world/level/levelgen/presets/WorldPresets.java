package net.minecraft.world.level.levelgen.presets;

import java.util.Optional;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.WorldDimensions;

public class WorldPresets {
   public static final ResourceKey<WorldPreset> NORMAL = register("normal");
   public static final ResourceKey<WorldPreset> FLAT = register("flat");
   public static final ResourceKey<WorldPreset> FLAT_ALL_DIMENSIONS = register("flat_all_dimensions");
   public static final ResourceKey<WorldPreset> LARGE_BIOMES = register("large_biomes");
   public static final ResourceKey<WorldPreset> AMPLIFIED = register("amplified");
   public static final ResourceKey<WorldPreset> SINGLE_BIOME_SURFACE = register("single_biome_surface");
   public static final ResourceKey<WorldPreset> DEBUG = register("debug_all_block_states");

   public static void bootstrap(final BootstrapContext<WorldPreset> context) {
      new WorldPresets$Bootstrap(context).bootstrap();
   }

   private static ResourceKey<WorldPreset> register(final String name) {
      return ResourceKey.create(Registries.WORLD_PRESET, Identifier.withDefaultNamespace(name));
   }

   public static Optional<ResourceKey<WorldPreset>> fromSettings(final WorldDimensions dimensions) {
      return dimensions.get(LevelStem.OVERWORLD).flatMap(levelStem -> {
         return switch (levelStem.generator()) {
            case FlatLevelSource ignored -> Optional.of(FLAT);
            case DebugLevelSource ignoredx -> Optional.of(DEBUG);
            case NoiseBasedChunkGenerator ignoredxx -> Optional.of(NORMAL);
            default -> Optional.empty();
         };
      });
   }

   public static WorldDimensions createNormalWorldDimensions(final HolderLookup$Provider registries) {
      return registries.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(NORMAL).value().createWorldDimensions();
   }

   public static LevelStem getNormalOverworld(final HolderLookup$Provider registries) {
      return registries.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(NORMAL).value().overworld().orElseThrow();
   }

   public static WorldDimensions createTestWorldDimensions(final HolderLookup$Provider registries) {
      return registries.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(FLAT_ALL_DIMENSIONS).value().createWorldDimensions();
   }
}
