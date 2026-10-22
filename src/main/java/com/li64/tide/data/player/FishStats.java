package com.li64.tide.data.player;

import com.li64.tide.data.angler.CatchQuality;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FishStats {
    public static final Codec<FishStats> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount_caught").forGetter(FishStats::getAmountCaught),
            CatchTimestamp.CODEC.optionalFieldOf("initial_catch_date").forGetter(FishStats::getInitialCatchDate),
            Codec.DOUBLE.fieldOf("largest_catch").forGetter(FishStats::getLargestCatch),
            Codec.DOUBLE.fieldOf("smallest_catch").forGetter(FishStats::getSmallestCatch),
            Codec.INT.optionalFieldOf("best_quality", -1).forGetter(FishStats::getBestQuality),
            Codec.DOUBLE.optionalFieldOf("heaviest_catch", 0.0).forGetter(FishStats::getHeaviestCatch),
            Codec.INT.listOf().optionalFieldOf("quality_counts", List.of()).forGetter(FishStats::getQualityCounts)
    ).apply(instance, FishStats::new));

    private int amountCaught;
    private Optional<CatchTimestamp> initialCatchDate;
    private double largestCatch;
    private double smallestCatch;
    private int bestQuality;
    private double heaviestCatch;
    private final int[] qualityCounts = new int[CatchQuality.values().length];

    public FishStats() {
        this(0, Optional.empty(), 0, Double.POSITIVE_INFINITY, -1, 0.0, List.of());
    }

    public FishStats(int amountCaught, Optional<CatchTimestamp> initialCatchDate, double largestCatch, double smallestCatch) {
        this(amountCaught, initialCatchDate, largestCatch, smallestCatch, -1, 0.0, List.of());
    }

    public FishStats(int amountCaught, Optional<CatchTimestamp> initialCatchDate, double largestCatch, double smallestCatch,
                     int bestQuality, double heaviestCatch, List<Integer> qualityCounts) {
        this.amountCaught = amountCaught;
        this.initialCatchDate = initialCatchDate;
        this.largestCatch = largestCatch;
        this.smallestCatch = smallestCatch;
        this.bestQuality = bestQuality;
        this.heaviestCatch = heaviestCatch;
        for (int i = 0; i < Math.min(qualityCounts.size(), this.qualityCounts.length); i++)
            this.qualityCounts[i] = qualityCounts.get(i);
    }

    public int getAmountCaught() {
        return amountCaught;
    }

    public Optional<CatchTimestamp> getInitialCatchDate() {
        return initialCatchDate;
    }

    public double getLargestCatch() {
        return largestCatch;
    }

    public double getSmallestCatch() {
        return smallestCatch;
    }

    /** -1 when no graded fish has been caught yet. */
    public int getBestQuality() {
        return bestQuality;
    }

    /** Grams; 0 when none weighed. */
    public double getHeaviestCatch() {
        return heaviestCatch;
    }

    public List<Integer> getQualityCounts() {
        List<Integer> list = new ArrayList<>();
        for (int c : qualityCounts) list.add(c);
        return list;
    }

    public int getQualityCount(CatchQuality q) {
        return qualityCounts[q.ordinal()];
    }

    public boolean isEmpty() {
        return amountCaught == 0;
    }

    public void logCatch(CatchTimestamp timestamp, double size) {
        logCatch(timestamp, size, -1, 0.0);
    }

    public void logCatch(CatchTimestamp timestamp, double size, int quality, double grams) {
        this.amountCaught++;
        if (initialCatchDate.isEmpty()) this.initialCatchDate = Optional.of(timestamp);
        if (size > 0.0) {
            if (largestCatch <= 0.0 || size > largestCatch) this.largestCatch = size;
            if (smallestCatch <= 0.0 || size < smallestCatch) this.smallestCatch = size;
        }
        if (quality >= 0 && quality < qualityCounts.length) {
            qualityCounts[quality]++;
            if (quality > bestQuality) bestQuality = quality;
        }
        if (grams > heaviestCatch) heaviestCatch = grams;
    }
}
