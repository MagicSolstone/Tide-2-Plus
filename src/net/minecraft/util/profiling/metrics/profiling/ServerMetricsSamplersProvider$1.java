package net.minecraft.util.profiling.metrics.profiling;

import com.google.common.base.Ticker;
import java.util.function.LongSupplier;

class ServerMetricsSamplersProvider$1 extends Ticker {
   ServerMetricsSamplersProvider$1(final LongSupplier val$timeSource) {
      this.val$timeSource = val$timeSourcex;
   }

   public long read() {
      return this.val$timeSource.getAsLong();
   }
}
