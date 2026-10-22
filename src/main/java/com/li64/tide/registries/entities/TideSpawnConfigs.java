package com.li64.tide.registries.entities;

import com.li64.tide.Tide;
import com.li64.tide.data.TideTags;
import com.li64.tide.registries.entities.fish.TideFishEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Consumer;

public class TideSpawnConfigs {
    public static void onRegisterSpawnConfigs(Consumer<SpawnConfig> registry) {
        // freshwater
        registry.accept(waterSpawnConfig("rainbow_trout", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("brook_trout", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("largemouth_bass", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("smallmouth_bass", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("white_crappie", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("black_crappie", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("yellow_perch", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("carp", 10, 3, 2, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("pike", 10, 3, 1, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("bluegill", 10, 5, 3, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("guppy", 6, 3, 2, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("catfish", 6, 3, 2, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("walleye", 6, 3, 2, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("arapaima", 3, 1, 1, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("mirage_catfish", 3, 1, 1, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("sand_tiger_shark", 3, 1, 1, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("slimy_salmon", 3, 2, 1, TideTags.Biomes.HAS_SWAMP_FISH));
        registry.accept(waterSpawnConfig("frostbite_flounder", 3, 1, 1, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("blossom_bass", 3, 1, 1, TideTags.Biomes.HAS_CHERRY_GROVE_FISH));
        registry.accept(waterSpawnConfig("sturgeon", 3, 1, 1, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("mooneye", 1, 1, 1, BiomeTags.IS_RIVER));
        registry.accept(waterSpawnConfig("bull_shark", 1, 1, 1, BiomeTags.IS_RIVER));

        // saltwater
        registry.accept(waterSpawnConfig("ocean_perch", 10, 5, 3, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("red_snapper", 10, 5, 3, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("flounder", 10, 3, 2, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("anchovy", 10, 6, 6, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("tuna", 10, 4, 2, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("mackerel", 10, 5, 3, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("snook", 10, 5, 3, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("angelfish", 6, 5, 3, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("mahi_mahi", 2, 2, 1, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("sailfish", 2, 2, 1, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("swordfish", 2, 2, 1, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("manta_ray", 2, 1, 1, BiomeTags.IS_OCEAN));
        registry.accept(waterSpawnConfig("aquathorn", 10, 5, 3, BiomeTags.IS_DEEP_OCEAN));
        registry.accept(waterSpawnConfig("great_white_shark", 1, 1, 1, BiomeTags.IS_OCEAN));

        // underground
        registry.accept(waterSpawnConfig("cave_eel", 6, 1, 1, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("cave_crawler", 6, 3, 2, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("deep_grouper", 6, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("shadow_snapper", 6, 4, 2, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("glowfish", 4, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("anglerfish", 4, 1, 1, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("abyss_angler", 4, 1, 1, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("iron_tetra", 4, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("lapis_lanternfish", 4, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("crystal_shrimp", 4, 3, 2, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("luminescent_jellyfish", 2, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("crystalline_carp", 2, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("gilded_minnow", 2, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("bedrock_tetra", 2, 5, 3, BiomeTags.IS_OVERWORLD));
        registry.accept(waterSpawnConfig("dripstone_darter", 8, 3, 2, TideTags.Biomes.HAS_DRIPSTONE_FISH));

        // lava
        registry.accept(lavaSpawnConfig("magma_mackerel", 10, 5, 3, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("ember_koi", 10, 5, 3, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("ash_perch", 10, 5, 3, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("obsidian_pike", 6, 4, 2, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("volcano_tuna", 6, 5, 3, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("inferno_guppy", 3, 5, 3, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("warped_guppy", 3, 5, 3, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("crimson_fangjaw", 3, 4, 2, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("soulscale", 3, 5, 3, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("witherfin", 3, 4, 2, BiomeTags.IS_NETHER));
        registry.accept(lavaSpawnConfig("blazing_swordfish", 2, 1, 1, BiomeTags.IS_NETHER));

        // end
        registry.accept(voidSpawnConfig("pale_clubfish", 10, 5, 3, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("amber_rockfish", 10, 5, 3, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("enderfin", 10, 5, 3, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("chorus_cod", 6, 5, 3, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("ender_glider", 6, 4, 2, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("endergazer", 6, 5, 3, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("violet_carp", 6, 4, 2, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("red_40", 3, 5, 3, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("dutchman_sock", 3, 5, 3, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("elytrout", 3, 4, 2, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("mantyvern", 1, 1, 1, BiomeTags.IS_END));
        registry.accept(voidSpawnConfig("snatcher_squid", 1, 1, 1, BiomeTags.IS_END));
    }
    
    public static SpawnConfig waterSpawnConfig(String entity, int weight, int maxGroupSize, int minGroupSize, TagKey<Biome> biomes) {
        return new SpawnConfig(entity, weight, maxGroupSize, minGroupSize, MobCategory.WATER_AMBIENT,
                SpawnPlacementTypes.IN_WATER,
                TideFishEntity::checkWaterFishSpawnRules, biomes);
    }

    public static SpawnConfig lavaSpawnConfig(String entity, int weight, int maxGroupSize, int minGroupSize, TagKey<Biome> biomes) {
        return new SpawnConfig(entity, weight, maxGroupSize, minGroupSize, MobCategory.WATER_AMBIENT,
                SpawnPlacementTypes.IN_LAVA,
                TideFishEntity::checkLavaFishSpawnRules, biomes);
    }

    public static SpawnConfig voidSpawnConfig(String entity, int weight, int maxGroupSize, int minGroupSize, TagKey<Biome> biomes) {
        return new SpawnConfig(entity, weight, maxGroupSize, minGroupSize, MobCategory.WATER_AMBIENT,
                SpawnPlacementTypes.NO_RESTRICTIONS,
                TideFishEntity::checkVoidFishSpawnRules, biomes);
    }

    public record SpawnConfig(EntityType<Mob> entityType, int weight, int maxGroup, int minGroup, MobCategory mobCategory,
                              SpawnPlacementType placementType,
                              SpawnPlacements.SpawnPredicate<Mob> spawnPredicate, TagKey<Biome> biomes) {
        @SuppressWarnings("unchecked")
        public SpawnConfig(String entity, int weight, int maxGroupSize, int minGroupSize, MobCategory category,
                           SpawnPlacementType placementType,
                           SpawnPlacements.SpawnPredicate<Mob> predicate, TagKey<Biome> biomes) {
            this((EntityType<Mob>) BuiltInRegistries.ENTITY_TYPE.get(Tide.resource(entity)),
                    weight, maxGroupSize, minGroupSize, category, placementType, predicate, biomes);
        }
    }
}