package net.minecraft.util.profiling;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;

class FilledProfileResults$CounterCollector {
   private long selfValue;
   private long totalValue;
   private final Map<String, FilledProfileResults$CounterCollector> children = Maps.newHashMap();

   private FilledProfileResults$CounterCollector() {
   }

   public void addValue(final Iterator<String> path, final long value) {
      this.totalValue += value;
      if (!path.hasNext()) {
         this.selfValue += value;
      } else {
         this.children.computeIfAbsent(path.next(), k -> new FilledProfileResults$CounterCollector()).addValue(path, value);
      }
   }
}
