package net.minecraft.client.gui.components.debug;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class DebugEntryMemory$AllocationRateCalculator {
   private static final int UPDATE_INTERVAL_MS = 500;
   private static final List<GarbageCollectorMXBean> GC_MBEANS = ManagementFactory.getGarbageCollectorMXBeans();
   private long lastTime = 0L;
   private long lastHeapUsage = -1L;
   private long lastGcCounts = -1L;
   private long lastRate = 0L;

   private DebugEntryMemory$AllocationRateCalculator() {
   }

   private long bytesAllocatedPerSecond(final long currentHeapUsage) {
      long time = System.currentTimeMillis();
      if (time - this.lastTime < 500L) {
         return this.lastRate;
      } else {
         long gcCounts = gcCounts();
         if (this.lastTime != 0L && gcCounts == this.lastGcCounts) {
            double multiplier = (double)TimeUnit.SECONDS.toMillis(1L) / (time - this.lastTime);
            long delta = currentHeapUsage - this.lastHeapUsage;
            this.lastRate = Math.round(delta * multiplier);
         }

         this.lastTime = time;
         this.lastHeapUsage = currentHeapUsage;
         this.lastGcCounts = gcCounts;
         return this.lastRate;
      }
   }

   private static long gcCounts() {
      long total = 0L;

      for (GarbageCollectorMXBean gcBean : GC_MBEANS) {
         total += gcBean.getCollectionCount();
      }

      return total;
   }
}
