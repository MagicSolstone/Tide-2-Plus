package net.minecraft.util.profiling.metrics;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

class MetricsRegistry$AggregatedMetricSampler extends MetricSampler {
   private final List<MetricSampler> delegates;

   private MetricsRegistry$AggregatedMetricSampler(final String name, final List<MetricSampler> delegates) {
      super(
         name,
         MetricSampler$SamplingPhase.END_TICK,
         delegates.get(0).getCategory(),
         () -> averageValueFromDelegates(delegates),
         () -> beforeTick(delegates),
         thresholdTest(delegates)
      );
      this.delegates = delegates;
   }

   private static MetricSampler$ThresholdTest thresholdTest(final List<MetricSampler> delegates) {
      return value -> delegates.stream().anyMatch(delegate -> delegate.thresholdTest != null ? delegate.thresholdTest.test(value) : false);
   }

   private static void beforeTick(final List<MetricSampler> delegates) {
      for (MetricSampler delegate : delegates) {
         delegate.onStartTick();
      }
   }

   private static double averageValueFromDelegates(final List<MetricSampler> delegates) {
      double aggregatedValue = 0.0;

      for (MetricSampler delegate : delegates) {
         aggregatedValue += delegate.getSampler().getAsDouble();
      }

      return aggregatedValue / delegates.size();
   }

   @Override
   public boolean equals(@Nullable final Object o) {
      if (this == o) {
         return true;
      } else if (o == null || this.getClass() != o.getClass()) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         MetricsRegistry$AggregatedMetricSampler that = (MetricsRegistry$AggregatedMetricSampler)o;
         return this.delegates.equals(that.delegates);
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), this.delegates);
   }
}
