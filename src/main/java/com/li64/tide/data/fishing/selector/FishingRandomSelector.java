package com.li64.tide.data.fishing.selector;

import com.li64.tide.data.fishing.CatchResult;
import com.li64.tide.data.fishing.FishingContext;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class FishingRandomSelector {
    public static <T extends FishingEntry> CatchResult select(List<T> entries, FishingContext context) {
        return select(entries, context, null, entry -> false);
    }

    public static <T extends FishingEntry> CatchResult select(List<T> entries, FishingContext context, Map<FishingEntry, Double> testResults, Predicate<FishingEntry> entryTest) {
        if (entries.isEmpty()) return CatchResult.empty();

        List<Pair<T, Double>> weightedValues = entries.stream()
                .filter(entry -> entry.shouldKeep(context))
                .map(entry -> new Pair<>(entry, entry.weight(context)))
                .filter(pair -> pair.getSecond() > 0) // remove 0-weight pairs
                .toList();
        weightedValues = com.li64.tide.data.rods.CutBait.apply(weightedValues, context);     // cut bait leans the fish toward the rare ones

        if (weightedValues.isEmpty()) return CatchResult.empty();
        if (testResults != null) weightedValues.forEach(pair -> {
            if (entryTest.test(pair.getFirst())) testResults.put(pair.getFirst(), pair.getSecond());
        });

        double randomValue = context.rng().nextDouble() * weightedValues
                .stream().mapToDouble(Pair::getSecond).sum();
        double cumulativeWeight = 0;

        for (Pair<T, Double> pair : weightedValues) {
            cumulativeWeight += pair.getSecond();
            if (randomValue <= cumulativeWeight) return pair.getFirst()
                    .getResult(context, testResults, entryTest);
        }
        return CatchResult.empty();
    }
}
