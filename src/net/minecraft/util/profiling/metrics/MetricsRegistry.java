package net.minecraft.util.profiling.metrics;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class MetricsRegistry {
   public static final MetricsRegistry INSTANCE = new MetricsRegistry();
   private final WeakHashMap<ProfilerMeasured, Void> measuredInstances = new WeakHashMap<>();

   private MetricsRegistry() {
   }

   public void add(final ProfilerMeasured profilerMeasured) {
      this.measuredInstances.put(profilerMeasured, null);
   }

   public List<MetricSampler> getRegisteredSamplers() {
      Map<String, List<MetricSampler>> samplersByName = this.measuredInstances
         .keySet()
         .stream()
         .flatMap(measuredInstance -> measuredInstance.profiledMetrics().stream())
         .collect(Collectors.groupingBy(MetricSampler::getName));
      return aggregateDuplicates(samplersByName);
   }

   private static List<MetricSampler> aggregateDuplicates(final Map<String, List<MetricSampler>> potentialDuplicates) {
      return potentialDuplicates.entrySet()
         .stream()
         .map(
            entry -> {
               String samplerName = entry.getKey();
               List<MetricSampler> duplicateSamplers = entry.getValue();
               return (MetricSampler)(duplicateSamplers.size() > 1
                  ? new MetricsRegistry$AggregatedMetricSampler(samplerName, duplicateSamplers)
                  : duplicateSamplers.get(0));
            }
         )
         .collect(Collectors.toList());
   }
}
