package net.minecraft.util.profiling.jfr.parse;

import net.minecraft.util.profiling.jfr.stats.IoSummary$CountAndSize;

public final class JfrStatsParser$MutableCountAndSize {
   private long count;
   private long totalSize;

   public void increment(final int bytes) {
      this.totalSize += bytes;
      this.count++;
   }

   public IoSummary$CountAndSize toCountAndSize() {
      return new IoSummary$CountAndSize(this.count, this.totalSize);
   }
}
