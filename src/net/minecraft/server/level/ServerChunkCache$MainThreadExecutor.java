package net.minecraft.server.level;

import java.util.Objects;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.Level;

final class ServerChunkCache$MainThreadExecutor extends BlockableEventLoop<Runnable> {
   private ServerChunkCache$MainThreadExecutor(final ServerChunkCache this$0, final Level level) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super("Chunk source main thread executor for " + level.dimension().identifier(), false);
   }

   @Override
   public Runnable wrapRunnable(final Runnable runnable) {
      return runnable;
   }

   @Override
   protected boolean shouldRun(final Runnable task) {
      return true;
   }

   @Override
   protected boolean scheduleExecutables() {
      return true;
   }

   @Override
   protected Thread getRunningThread() {
      return this.this$0.mainThread;
   }

   @Override
   protected void doRunTask(final Runnable task) {
      Profiler.get().incrementCounter("runTask");
      super.doRunTask(task);
   }

   @Override
   protected boolean pollTask() {
      if (this.this$0.runDistanceManagerUpdates()) {
         return true;
      } else {
         this.this$0.lightEngine.tryScheduleUpdate();
         return super.pollTask();
      }
   }
}
