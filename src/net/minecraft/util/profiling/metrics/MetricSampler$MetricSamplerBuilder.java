package net.minecraft.util.profiling.metrics;

import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;
import org.jspecify.annotations.Nullable;

public class MetricSampler$MetricSamplerBuilder<T> {
   private final String name;
   private final MetricCategory category;
   private final DoubleSupplier sampler;
   private final T context;
   private MetricSampler$SamplingPhase samplingPhase = MetricSampler$SamplingPhase.END_TICK;
   @Nullable
   private Runnable beforeTick;
   @Nullable
   private MetricSampler$ThresholdTest thresholdTest;

   public MetricSampler$MetricSamplerBuilder(final String name, final MetricCategory category, final ToDoubleFunction<T> sampler, final T context) {
      this.name = name;
      this.category = category;
      this.sampler = () -> sampler.applyAsDouble(context);
      this.context = context;
   }

   public MetricSampler$MetricSamplerBuilder<T> withBeforeTick(final Consumer<T> beforeTick) {
      this.beforeTick = () -> beforeTick.accept(this.context);
      return this;
   }

   public MetricSampler$MetricSamplerBuilder<T> withThresholdAlert(final MetricSampler$ThresholdTest thresholdTest) {
      this.thresholdTest = thresholdTest;
      return this;
   }

   public MetricSampler$MetricSamplerBuilder<T> withSamplingPhase(final MetricSampler$SamplingPhase samplingPhase) {
      this.samplingPhase = samplingPhase;
      return this;
   }

   public MetricSampler build() {
      return new MetricSampler(this.name, this.samplingPhase, this.category, this.sampler, this.beforeTick, this.thresholdTest);
   }
}
