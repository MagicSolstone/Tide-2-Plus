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

    /**
     * For a lure's tooltip, one line: "Favorite Lure: Bass, Shark, ???, ???". The fish you've caught (in your journal)
     * by name, the rest as ??? so you can see how many are left to find by fishing with it.
     */
    public static List<Component> describe(ItemStack lure) {
        List<String> fish = FISH_OF.get(BuiltInRegistries.ITEM.getKey(lure.getItem()).toString());
        if (fish == null || fish.isEmpty()) return List.of();
        List<String> names = new ArrayList<>();
        for (String id : fish)
            names.add(BuiltInRegistries.ITEM.getOptional(Identifier.parse(id))
                    .filter(i -> com.li64.tide.data.player.TidePlayerData.CLIENT_DATA.isFishUnlocked(BuiltInRegistries.ITEM.wrapAsHolder(i)))
                    .map(i -> Component.translatable(i.getDescriptionId()).getString()).orElse("???"));
        names.sort(java.util.Comparator.comparing(n -> n.equals("???")));      // the ones you know first
        List<Component> out = new ArrayList<>();
        for (int i = 0; i < names.size(); i += 4) {
            String part = String.join(", ", names.subList(i, Math.min(names.size(), i + 4))) + (i + 4 < names.size() ? "," : "");
            out.add(Component.literal(i == 0 ? "Favorite Lure: " + part : "  " + part).withStyle(ChatFormatting.DARK_AQUA));
        }
        return out;
    }

    /** How many fish that favor this lure could bite right here, right now. */
    public static int here(com.li64.tide.data.fishing.FishingContext context, ItemStack lure) {
        int n = 0;
        for (com.li64.tide.data.fishing.FishData d : com.li64.tide.data.TideData.FISH.get().values())
            if (isFavorite(d.fish().value(), lure) && d.shouldKeep(context)) n++;
        return n;
    }
}
