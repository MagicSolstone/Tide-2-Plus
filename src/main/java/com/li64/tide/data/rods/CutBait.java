package com.li64.tide.data.rods;

import com.li64.tide.data.angler.CatchQuality;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.journal.FishRarity;
import com.li64.tide.registries.TideItems;
import com.li64.tide.util.BaitUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Cut bait remembers the quality of the fish it was cut from, and better cut bait draws rarer fish: with it on the
 * rod, at least this share of bites (in water where rare fish live at all) are rare, very rare or legendary.
 * Poor 15%, Average 20%, Good 30%, Great 45%, Trophy 75%, Mythic every time. Where the water already gives more
 * than that, nothing changes. So a Poor or Average fish still has a use, and a Trophy is a real choice: keep it,
 * or cut it for four casts at the rare fish.
 */
public final class CutBait {
    private CutBait() {}

    public static double rareShare(CatchQuality q) {
        return switch (q) {
            case POOR -> 0.15;
            case AVERAGE -> 0.20;
            case GOOD -> 0.30;
            case GREAT -> 0.45;
            case TROPHY -> 0.75;
            case MYTHIC -> 1.0;
        };
    }

    /** The cut bait's quality (old cut bait without one counts as Average). */
    public static CatchQuality quality(ItemStack cutBait) {
        Integer q = TideItemData.FISH_QUALITY.getOrDefault(cutBait, null);
        return q == null ? CatchQuality.AVERAGE : CatchQuality.byId(q);
    }

    /** The best cut bait on the rod, or null if it has none. */
    public static CatchQuality best(ItemStack rod) {
        CatchQuality best = null;
        for (ItemStack s : BaitUtils.getBaitItems(rod))
            if (s.is(TideItems.CUT_BAIT) && (best == null || quality(s).ordinal() > best.ordinal())) best = quality(s);
        return best;
    }

    private static boolean rare(Object entry) {
        return entry instanceof FishData f && f.profile().rarity().ordinal() >= FishRarity.RARE.ordinal();
    }

    /** Re-weighs the fish that could bite here so the rare ones make up at least the cut bait's share. */
    public static <T> List<Pair<T, Double>> apply(List<Pair<T, Double>> weighted, FishingContext context) {
        if (context.rod() == null || weighted.isEmpty() || !(weighted.get(0).getFirst() instanceof FishData)) return weighted;
        CatchQuality q = best(context.rod());
        if (q == null) return weighted;
        double rareW = 0, otherW = 0;
        for (Pair<T, Double> p : weighted) if (rare(p.getFirst())) rareW += p.getSecond(); else otherW += p.getSecond();
        double target = rareShare(q);
        if (rareW <= 0 || rareW / (rareW + otherW) >= target) return weighted;
        List<Pair<T, Double>> out = new ArrayList<>();
        if (target >= 1.0) {                      // Mythic cut bait: only the rare fish
            for (Pair<T, Double> p : weighted) if (rare(p.getFirst())) out.add(p);
            return out;
        }
        double scale = target * otherW / ((1 - target) * rareW);
        for (Pair<T, Double> p : weighted) out.add(rare(p.getFirst()) ? new Pair<>(p.getFirst(), p.getSecond() * scale) : p);
        return out;
    }
}
