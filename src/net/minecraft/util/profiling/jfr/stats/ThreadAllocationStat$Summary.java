package net.minecraft.util.profiling.jfr.stats;

import java.util.Map;

public record ThreadAllocationStat$Summary(Map<String, Double> allocationsPerSecondByThread) {
}
