package com.li64.tide.data.rods;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.li64.tide.Tide;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Every fish has one favorite lure (data/tide/favorite_lures.json): with it on the rod, that fish is twice as
 * likely to be the one that bites. Any lure, or none, can still catch any fish.
 */
public final class FavoriteLures {
    private FavoriteLures() {}

    private static double factor = 2.0;
    private static final Map<String, String> LURE_OF = new HashMap<>();
    private static final Map<String, List<String>> FISH_OF = new LinkedHashMap<>();

    static {
        try (var in = FavoriteLures.class.getResourceAsStream("/data/tide/favorite_lures.json")) {
            JsonObject root = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            factor = root.get("factor").getAsDouble();
            for (var e : root.getAsJsonObject("lures").entrySet()) {
                List<String> fish = new ArrayList<>();
                e.getValue().getAsJsonArray().forEach(f -> { fish.add(f.getAsString()); LURE_OF.put(f.getAsString(), e.getKey()); });
                FISH_OF.put(e.getKey(), fish);
            }
        } catch (Exception e) {
            Tide.LOG.error("Couldn't read favorite_lures.json", e);
        }
    }

    public static double factor() {
        return factor;
    }

    /** The favorite lure of a fish (item id), or null. */
    public static String lureFor(Item fish) {
        return LURE_OF.get(BuiltInRegistries.ITEM.getKey(fish).toString());
    }

    public static boolean isFavorite(Item fish, ItemStack lure) {
        return !lure.isEmpty() && BuiltInRegistries.ITEM.getKey(lure.getItem()).toString().equals(lureFor(fish));
    }

    /** "Favorite of: Brook Trout, Bluegill, ..." for a lure's tooltip, a few names to a line. */
    public static List<Component> describe(ItemStack lure) {
        List<String> fish = FISH_OF.get(BuiltInRegistries.ITEM.getKey(lure.getItem()).toString());
        if (fish == null || fish.isEmpty()) return List.of();
        List<Component> out = new ArrayList<>();
        out.add(Component.literal("Favorite lure of these fish (x" + trim(factor) + " likely to bite):").withStyle(ChatFormatting.DARK_AQUA));
        List<String> names = fish.stream().map(id -> BuiltInRegistries.ITEM.getOptional(Identifier.parse(id))
                .map(i -> Component.translatable(i.getDescriptionId()).getString()).orElse(id)).toList();
        for (int i = 0; i < names.size(); i += 4)
            out.add(Component.literal("  " + String.join(", ", names.subList(i, Math.min(names.size(), i + 4)))).withStyle(ChatFormatting.DARK_AQUA));
        return out;
    }

    private static String trim(double v) {
        return v == Math.floor(v) ? String.valueOf((int) v) : String.valueOf(v);
    }
}
