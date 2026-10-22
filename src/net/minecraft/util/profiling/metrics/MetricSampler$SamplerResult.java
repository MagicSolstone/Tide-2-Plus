package net.minecraft.util.profiling.metrics;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;

public class MetricSampler$SamplerResult {
   private final Int2DoubleMap recording;
   private final int firstTick;
   private final int lastTick;

   public MetricSampler$SamplerResult(final int firstTick, final int lastTick, final Int2DoubleMap recording) {
      this.firstTick = firstTick;
      this.lastTick = lastTick;
      this.recording = recording;
   }

   public double valueAtTick(final int tick) {
      return this.recording.get(tick);
   }

   public int getFirstTick() {
      return this.firstTick;
   }

   public int getLastTick() {
      return this.lastTick;
   }
}
