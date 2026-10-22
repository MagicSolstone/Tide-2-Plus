package net.minecraft.util;

import java.util.concurrent.TimeUnit;

@FunctionalInterface
public interface TimeSource {
   long get(TimeUnit timeUnit);

   static TimeSource$NanoTimeSource constant(final long value) {
      return () -> value;
   }
}
