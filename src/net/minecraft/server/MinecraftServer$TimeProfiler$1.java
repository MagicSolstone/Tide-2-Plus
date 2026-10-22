package net.minecraft.server;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ResultField;

class MinecraftServer$TimeProfiler$1 implements ProfileResults {
   MinecraftServer$TimeProfiler$1(final MinecraftServer$TimeProfiler this$0, final long val$stopNanos, final int val$stopTick) {
      this.val$stopNanos = val$stopNanosx;
      this.val$stopTick = val$stopTickx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public List<ResultField> getTimes(final String path) {
      return Collections.emptyList();
   }

   @Override
   public boolean saveResults(final Path file) {
      return false;
   }

   @Override
   public long getStartTimeNano() {
      return this.this$0.startNanos;
   }

   @Override
   public int getStartTimeTicks() {
      return this.this$0.startTick;
   }

   @Override
   public long getEndTimeNano() {
      return this.val$stopNanos;
   }

   @Override
   public int getEndTimeTicks() {
      return this.val$stopTick;
   }

   @Override
   public String getProfilerResults() {
      return "";
   }
}
