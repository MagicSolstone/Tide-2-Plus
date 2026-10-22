package net.minecraft.util.profiling;

import com.mojang.jtracy.Plot;
import com.mojang.jtracy.TracyClient;

final class TracyZoneFiller$PlotAndValue {
   private final Plot plot;
   private int value;

   private TracyZoneFiller$PlotAndValue(final String name) {
      this.plot = TracyClient.createPlot(name);
      this.value = 0;
   }

   public void set(final int value) {
      this.value = value;
      this.plot.setValue(value);
   }

   public void add(final int amount) {
      this.set(this.value + amount);
   }
}
