package net.minecraft.server;

import net.minecraft.util.profiling.ProfileResults;

class MinecraftServer$TimeProfiler {
   private final long startNanos;
   private final int startTick;

   private MinecraftServer$TimeProfiler(final long startNanos, final int startTick) {
      this.startNanos = startNanos;
      this.startTick = startTick;
   }

   private ProfileResults stop(final long stopNanos, final int stopTick) {
      return new MinecraftServer$TimeProfiler$1(this, stopNanos, stopTick);
   }
}
