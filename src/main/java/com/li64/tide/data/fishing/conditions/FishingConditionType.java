package com.li64.tide.data.fishing.conditions;

import com.li64.tide.Tide;
import com.li64.tide.data.fishing.conditions.types.*;
import com.li64.tide.registries.TideRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface FishingConditionType<T extends FishingCondition> {
    FishingConditionType<EitherCondition> EITHER = register("either", EitherCondition.CODEC);
    FishingConditionType<NotCondition> NOT = register("not", NotCondition.CODEC);
    FishingConditionType<FreshwaterCondition> FRESHWATER = register("freshwater", FreshwaterCondition.CODEC);
    FishingConditionType<SaltwaterCondition> SALTWATER = register("saltwater", SaltwaterCondition.CODEC);
    FishingConditionType<DimensionsCondition> DIMENSION = register("dimension", DimensionsCondition.CODEC);
    FishingConditionType<BiomeWhitelistCondition> BIOME_WHITELIST = register("found_in", BiomeWhitelistCondition.CODEC);
    FishingConditionType<TimeOfDayCondition> TIME_OF_DAY = register("time_of_day", TimeOfDayCondition.CODEC);
    FishingConditionType<FishingMediumCondition> MEDIUM = register("fluid", FishingMediumCondition.CODEC);
    FishingConditionType<AboveCondition> ABOVE = register("above", AboveCondition.CODEC);
    FishingConditionType<BelowCondition> BELOW = register("below", BelowCondition.CODEC);
    FishingConditionType<DepthRangeCondition> DEPTH_RANGE = register("depth_range", DepthRangeCondition.CODEC);
    FishingConditionType<StructuresCondition> STRUCTURES = register("found_in_structures", StructuresCondition.CODEC);
    FishingConditionType<LuckCondition> LUCK = register("luck", LuckCondition.CODEC);
    FishingConditionType<MoonPhaseCondition> MOON_PHASE = register("moon_phase", MoonPhaseCondition.CODEC);
    FishingConditionType<WeatherCondition> WEATHER = register("weather", WeatherCondition.CODEC);
    FishingConditionType<OpenWaterCondition> OPEN_WATER = register("open_water", OpenWaterCondition.CODEC);
    FishingConditionType<BlockNearbyCondition> BLOCK_NEARBY = register("block_nearby", BlockNearbyCondition.CODEC);
    FishingConditionType<SeasonsCondition> SEASONS = register("seasons", SeasonsCondition.CODEC);
    FishingConditionType<EnchantmentsCondition> HAS_ENCHANTMENTS = register("has_enchantments", EnchantmentsCondition.CODEC);

    static void register() {
        Tide.LOG.info("Registering fishing conditions");
    }

    MapCodec<T> codec();

    static <T extends FishingCondition> FishingConditionType<T> register(String name, MapCodec<T> codec) {
        return register(Tide.resource(name), codec);
    }

    static <T extends FishingCondition> FishingConditionType<T> register(ResourceLocation name, MapCodec<T> codec) {
        return Registry.register(TideRegistries.FISHING_CONDITIONS, name, () -> codec);
    }
}