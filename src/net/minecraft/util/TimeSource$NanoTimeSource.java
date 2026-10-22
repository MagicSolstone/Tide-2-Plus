package net.minecraft.util;

import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public interface TimeSource$NanoTimeSource extends LongSupplier, TimeSource {
   @Override
   default long get(final TimeUnit timeUnit) {
      return timeUnit.convert(this.getAsLong(), TimeUnit.NANOSECONDS);
   }
}
