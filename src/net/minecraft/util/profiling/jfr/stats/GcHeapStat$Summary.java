package net.minecraft.util.profiling.jfr.stats;

import java.time.Duration;

public record GcHeapStat$Summary(Duration duration, Duration gcTotalDuration, int totalGCs, double allocationRateBytesPerSecond) {
   public float gcOverHead() {
      return (float)this.gcTotalDuration.toMillis() / (float)this.duration.toMillis();
   }
}
