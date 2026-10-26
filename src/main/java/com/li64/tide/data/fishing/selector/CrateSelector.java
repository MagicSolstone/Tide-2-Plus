package com.li64.tide.data.fishing.selector;

import com.li64.tide.Tide;
import com.li64.tide.data.TideData;
import com.li64.tide.data.fishing.CatchResult;
import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.registries.TideItems;
import com.li64.tide.util.BaitUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Map;
import java.util.function.Predicate;

public class CrateSelector implements FishingEntry {
    private static final double MAGNETIC_BAIT_BONUS = 5.0;
    /** Luck adds up to this much (+4% a point, +40% at LUCK_CAP and above); crate gear can't push past MAX_FACTOR times the base. */
    private static final double MAX_LUCK_BONUS = 0.4, MAX_FACTOR = 6.0;
    private static final int LUCK_CAP = 10;

    @Override
    public double weight(FishingContext context) {
        // Crates stay a treat: luck helps a little and stops helping at luck 10, and
        // even with crate gear (magnet lure, magnetic bait) they're capped at about 1 catch in 5.
        boolean hasMagneticBait = context.rod() != null && BaitUtils.hasBait(TideItems.MAGNETIC_BAIT, context.rod());
        double base = Tide.SERVER_CONFIG.general.crateWeight;
        double weight = base + MAX_LUCK_BONUS * base * Math.min(Math.max(context.luck(), 0), LUCK_CAP) / LUCK_CAP
                * Tide.SERVER_CONFIG.general.crateQuality;
        weight *= (hasMagneticBait ? MAGNETIC_BAIT_BONUS : 1.0) * com.li64.tide.data.rods.Lures.crateFactor(context);
        return Math.min(weight, base * MAX_FACTOR);
    }

    @Override
    public boolean shouldKeep(FishingContext context) {
        return getResult(context).isPresent();
    }

    @Override
    public CatchResult getResult(FishingContext context) {
        return this.getResult(context, null, entry -> false);
    }

    @Override
    public CatchResult getResult(FishingContext context, Map<FishingEntry, Double> results, Predicate<FishingEntry> entryTest) {
        return FishingRandomSelector.select(TideData.CRATES.get().values(), context, results, entryTest);
    }

    @Override
    public MutableComponent getTestKey() {
        return Component.translatable("commands.fishing.entries.crate_selector");
    }
}
