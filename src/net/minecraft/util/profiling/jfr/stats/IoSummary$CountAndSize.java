package net.minecraft.util.profiling.jfr.stats;

import java.util.Comparator;

public record IoSummary$CountAndSize(long totalCount, long totalSize) {
   private static final Comparator<IoSummary$CountAndSize> SIZE_THEN_COUNT = Comparator.comparing(IoSummary$CountAndSize::totalSize)
      .thenComparing(IoSummary$CountAndSize::totalCount)
      .reversed();

   public IoSummary$CountAndSize add(final IoSummary$CountAndSize that) {
      return new IoSummary$CountAndSize(this.totalCount + that.totalCount, this.totalSize + that.totalSize);
   }

   public float averageSize() {
      return (float)this.totalSize / (float)this.totalCount;
   }
}
