package com.li64.tide.data.angler;

import com.li64.tide.data.item.TideItemData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * Fish crafted into resources (an iron tetra into raw iron, a glowfish into glow ink...) give more for a better fish:
 * the recipe's amount is an Average fish's; Poor gives half (at least 1), Good x1.5, Great x2, Trophy x3, Mythic x4
 * (rounded up). A fish without a quality (caught before qualities existed) gives the recipe's amount.
 */
public final class FishResources {
    private FishResources() {}

    public static final Set<String> FISH = Set.of("iron_tetra", "glowfish", "gilded_minnow", "lapis_lanternfish",
            "crystal_shrimp", "enderfin", "endergazer", "elytrout");
    private static final double[] FACTOR = {0.5, 1.0, 1.5, 2.0, 3.0, 4.0};

    public static int amount(int base, int quality) {
        if (quality < 0 || quality >= FACTOR.length) return base;
        return Math.max(1, (int) Math.ceil(base * FACTOR[quality] - 1e-9));
    }

    /** After a shapeless recipe is assembled: scale it when its only ingredient is one of these fish. */
    public static void scale(List<ItemStack> inputs, ItemStack result) {
        if (result.isEmpty()) return;
        ItemStack fish = null;
        for (ItemStack s : inputs) {
            if (s.isEmpty()) continue;
            if (fish != null) return;
            fish = s;
        }
        if (fish == null || !BuiltInRegistries.ITEM.getKey(fish.getItem()).getNamespace().equals("tide")
                || !FISH.contains(BuiltInRegistries.ITEM.getKey(fish.getItem()).getPath())) return;
        Integer q = TideItemData.FISH_QUALITY.getOrDefault(fish, null);
        if (q == null) return;
        result.setCount(Math.min(result.getMaxStackSize(), amount(result.getCount(), q)));
    }
}
