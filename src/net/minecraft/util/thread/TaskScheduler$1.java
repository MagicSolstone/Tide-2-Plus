package net.minecraft.util.thread;

import java.util.concurrent.Executor;

class TaskScheduler$1 implements TaskScheduler<Runnable> {
   TaskScheduler$1(final String val$name, final Executor val$executor) {
      this.val$name = val$namex;
      this.val$executor = val$executorx;
   }

   @Override
   public String name() {
      return this.val$name;
   }

   @Override
   public void schedule(final Runnable runnable) {
      this.val$executor.execute(runnable);
   }

   @Override
   public Runnable wrapRunnable(final Runnable runnable) {
      return runnable;
   }

   @Override
   public String toString() {
      return this.val$name;
   }
}
