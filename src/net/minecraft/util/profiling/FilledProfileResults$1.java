package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;

class FilledProfileResults$1 implements ProfilerPathEntry {
   @Override
   public long getDuration() {
      return 0L;
   }

   @Override
   public long getMaxDuration() {
      return 0L;
   }

   @Override
   public long getCount() {
      return 0L;
   }

   @Override
   public Object2LongMap<String> getCounters() {
      return Object2LongMaps.emptyMap();
   }
}
