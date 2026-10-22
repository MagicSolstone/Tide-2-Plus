package net.minecraft.util.profiling.metrics.profiling;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class ServerMetricsSamplersProvider$CpuStats {
   private final SystemInfo systemInfo = new SystemInfo();
   private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();
   public final int nrOfCpus = this.processor.getLogicalProcessorCount();
   private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
   private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
   private long lastPollMs;

   public double loadForCpu(final int i) {
      long now = System.currentTimeMillis();
      if (this.lastPollMs == 0L || this.lastPollMs + 501L < now) {
         this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
         this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
         this.lastPollMs = now;
      }

      return this.currentLoad[i] * 100.0;
   }
}
