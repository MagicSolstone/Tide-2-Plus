package net.minecraft.util.profiling.metrics;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import java.util.Locale;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;
import org.jspecify.annotations.Nullable;

public class MetricSampler {
   private final String name;
   private final MetricSampler$SamplingPhase samplingPhase;
   private final MetricCategory category;
   private final DoubleSupplier sampler;
   private final ByteBuf ticks;
   private final ByteBuf values;
   private volatile boolean isRunning;
   @Nullable
   private final Runnable beforeTick;
   @Nullable
   final MetricSampler$ThresholdTest thresholdTest;
   private double currentValue;

   protected MetricSampler(
      final String name,
      final MetricSampler$SamplingPhase samplingPhase,
      final MetricCategory category,
      final DoubleSupplier sampler,
      @Nullable final Runnable beforeTick,
      @Nullable final MetricSampler$ThresholdTest thresholdTest
   ) {
      this.name = name;
      this.samplingPhase = samplingPhase;
      this.category = category;
      this.beforeTick = beforeTick;
      this.sampler = sampler;
      this.thresholdTest = thresholdTest;
      this.values = ByteBufAllocator.DEFAULT.buffer();
      this.ticks = ByteBufAllocator.DEFAULT.buffer();
      this.isRunning = true;
   }

   public static MetricSampler create(final String name, final MetricCategory category, final DoubleSupplier sampler) {
      return new MetricSampler(name, MetricSampler$SamplingPhase.END_TICK, category, sampler, null, null);
   }

   public static MetricSampler createExtractSampler(final String name, final MetricCategory category, final DoubleSupplier sampler) {
      return new MetricSampler(name, MetricSampler$SamplingPhase.EXTRACT, category, sampler, null, null);
   }

   public static <T> MetricSampler$MetricSamplerBuilder<T> builder(
      final String metricName, final MetricCategory category, final ToDoubleFunction<T> sampler, final T context
   ) {
      if (sampler == null) {
         throw new IllegalStateException();
      } else {
         return new MetricSampler$MetricSamplerBuilder<>(metricName, category, sampler, context);
      }
   }

   public void onStartTick() {
      if (!this.isRunning) {
         throw new IllegalStateException("Not running");
      } else {
         if (this.beforeTick != null) {
            this.beforeTick.run();
         }
      }
   }

   public void onEndTick(final int currentTick) {
      this.verifyRunning();
      this.currentValue = this.sampler.getAsDouble();
      this.values.writeDouble(this.currentValue);
      this.ticks.writeInt(currentTick);
   }

   public void onFinished() {
      this.verifyRunning();
      this.values.release();
      this.ticks.release();
      this.isRunning = false;
   }

   private void verifyRunning() {
      if (!this.isRunning) {
         throw new IllegalStateException(String.format(Locale.ROOT, "Sampler for metric %s not started!", this.name));
      }
   }

   public DoubleSupplier getSampler() {
      return this.sampler;
   }

   public String getName() {
      return this.name;
   }

   public MetricSampler$SamplingPhase samplingPhase() {
      return this.samplingPhase;
   }

   public MetricCategory getCategory() {
      return this.category;
   }

   public MetricSampler$SamplerResult result() {
      Int2DoubleMap result = new Int2DoubleOpenHashMap();
      int firstTick = Integer.MIN_VALUE;
      int lastTick = Integer.MIN_VALUE;

      while (this.values.isReadable(8)) {
         int currentTick = this.ticks.readInt();
         if (firstTick == Integer.MIN_VALUE) {
            firstTick = currentTick;
         }

         result.put(currentTick, this.values.readDouble());
         lastTick = currentTick;
      }

      return new MetricSampler$SamplerResult(firstTick, lastTick, result);
   }

   public boolean triggersThreshold() {
      return this.thresholdTest != null && this.thresholdTest.test(this.currentValue);
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MetricSampler that = (MetricSampler)o;
         return this.name.equals(that.name) && this.category.equals(that.category);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.name.hashCode();
   }
}
