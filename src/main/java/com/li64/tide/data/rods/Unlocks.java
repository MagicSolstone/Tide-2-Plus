package com.li64.tide.data.rods;

import com.li64.tide.data.fishing.FishingContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Gear that lets fish bite outside their usual place or time: a region's fish in any water ("biome:has_jungle_fish",
 * or "biome:*" for every region), any time of day ("time"), any weather ("weather"), any moon ("moon"), or saltwater
 * and freshwater fish in either ("water"). The fish's other needs (depth, dimension, lava...) still apply.
 */
public final class Unlocks {
    private Unlocks() {}

    private static final Map<String, Set<String>> BY_ITEM = Map.ofEntries(
            Map.entry("wayfinder_fishing_rod", Set.of("biome:*")),
            Map.entry("conduit_fishing_rod", Set.of("water")),
            Map.entry("clockwork_fishing_rod", Set.of("time")),
            Map.entry("storm_lure", Set.of("weather")),
            Map.entry("squid_lure", Set.of("water")),
            Map.entry("moon_bait", Set.of("moon")),
            Map.entry("brine_shrimp", Set.of("water")),
            Map.entry("jungle_bait", Set.of("biome:has_jungle_fish")),
            Map.entry("swamp_bait", Set.of("biome:has_swamp_fish")),
            Map.entry("desert_bait", Set.of("biome:has_desert_fish")),
            Map.entry("frost_bait", Set.of("biome:has_frozen_fish")),
            Map.entry("spore_bait", Set.of("biome:has_mushroom_fish")),
            Map.entry("blossom_bait", Set.of("biome:has_cherry_grove_fish")),
            Map.entry("coastal_bait", Set.of("biome:has_coastal_fish")),
            Map.entry("meadow_bait", Set.of("biome:has_plains_fish")),
            Map.entry("rock_bait", Set.of("biome:has_rocky_fish")),
            Map.entry("dripstone_bait", Set.of("biome:has_dripstone_fish")),
            Map.entry("sculk_bait", Set.of("biome:has_deep_dark_fish")));

    private static final Map<String, String> TEXT = Map.ofEntries(
            Map.entry("biome:*", "Fish from every region can bite in any water"),
            Map.entry("water", "Saltwater and freshwater fish bite in either"),
            Map.entry("time", "Day and night fish bite at any time"),
            Map.entry("weather", "Rain and storm fish bite in any weather"),
            Map.entry("moon", "Moon-phase fish bite on any night"),
            Map.entry("biome:has_jungle_fish", "Jungle fish bite in any water"),
            Map.entry("biome:has_swamp_fish", "Swamp fish bite in any water"),
            Map.entry("biome:has_desert_fish", "Desert fish bite in any water"),
            Map.entry("biome:has_frozen_fish", "Frozen-biome fish bite in any water"),
            Map.entry("biome:has_mushroom_fish", "Mushroom island fish bite in any water"),
            Map.entry("biome:has_cherry_grove_fish", "Cherry grove fish bite in any water"),
            Map.entry("biome:has_coastal_fish", "Coastal fish bite in any water"),
            Map.entry("biome:has_plains_fish", "Plains fish bite in any water"),
            Map.entry("biome:has_rocky_fish", "Rocky-shore fish bite in any water"),
            Map.entry("biome:has_dripstone_fish", "Dripstone cave fish bite in any water"),
            Map.entry("biome:has_deep_dark_fish", "Deep dark fish bite in any water"));

    private static String path(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
    }

    private static boolean any(FishingContext context, java.util.function.Predicate<Set<String>> test) {
        if (context == null || context.rod() == null || context.rod().isEmpty()) return false;
        List<ItemStack> all = new ArrayList<>(List.of(context.rod()));
        all.addAll(RodEffects.parts(context.rod()));
        for (ItemStack part : all) {
            Set<String> keys = BY_ITEM.get(path(part));
            if (keys != null && test.test(keys)) return true;
        }
        return false;
    }

    /** Whether something on the rod unlocks this ("time", "weather", "moon", "water"). */
    public static boolean has(FishingContext context, String key) {
        return any(context, keys -> keys.contains(key));
    }

    /** Whether something on the rod lets fish from one of these biome tags bite anywhere. */
    public static boolean biome(FishingContext context, List<TagKey<Biome>> tags) {
        return any(context, keys -> keys.contains("biome:*")
                || tags.stream().anyMatch(t -> keys.contains("biome:" + t.location().getPath())));
    }

    public static List<Component> describe(ItemStack stack) {
        List<Component> out = new ArrayList<>();
        for (String key : BY_ITEM.getOrDefault(path(stack), Set.of()))
            out.add(Component.literal(TEXT.getOrDefault(key, key)).withStyle(ChatFormatting.LIGHT_PURPLE));
        return out;
    }
}
