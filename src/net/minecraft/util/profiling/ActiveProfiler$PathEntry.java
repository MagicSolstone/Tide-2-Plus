package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

public class ActiveProfiler$PathEntry implements ProfilerPathEntry {
   private long maxDuration = Long.MIN_VALUE;
   private long minDuration = Long.MAX_VALUE;
   private long accumulatedDuration;
   private long count;
   private final Object2LongOpenHashMap<String> counters = new Object2LongOpenHashMap();

   @Override
   public long getDuration() {
      return this.accumulatedDuration;
   }

   @Override
   public long getMaxDuration() {
      return this.maxDuration;
   }

   @Override
   public long getCount() {
      return this.count;
   }

   @Override
   public Object2LongMap<String> getCounters() {
      return Object2LongMaps.unmodifiable(this.counters);
   }
}
