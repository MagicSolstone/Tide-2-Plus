package net.minecraft.util.profiling.metrics;

public class MetricSampler$ValueIncreasedByPercentage implements MetricSampler$ThresholdTest {
   private final float percentageIncreaseThreshold;
   private double previousValue = Double.MIN_VALUE;

   public MetricSampler$ValueIncreasedByPercentage(final float percentageIncreaseThreshold) {
      this.percentageIncreaseThreshold = percentageIncreaseThreshold;
   }

   @Override
   public boolean test(final double value) {
      boolean result;
      if (this.previousValue != Double.MIN_VALUE && !(value <= this.previousValue)) {
         result = (value - this.previousValue) / this.previousValue >= this.percentageIncreaseThreshold;
      } else {
         result = false;
      }

      this.previousValue = value;
      return result;
   }
}
