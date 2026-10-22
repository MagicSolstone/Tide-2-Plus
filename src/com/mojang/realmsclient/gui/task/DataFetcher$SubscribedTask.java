package com.mojang.realmsclient.gui.task;

import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class DataFetcher$SubscribedTask<T> {
   private final DataFetcher$Task<T> task;
   private final Consumer<T> output;
   private long lastCheckTime;

   private DataFetcher$SubscribedTask(final DataFetcher$Task<T> this$0, final Consumer<T> task, final Consumer output) {
      Objects.requireNonNull(this$0);
      super();
      this.lastCheckTime = -1L;
      this.task = task;
      this.output = output;
   }

   private void update(final long currentTime) {
      this.task.updateIfNeeded(currentTime);
      this.runCallbackIfNeeded();
   }

   private void runCallbackIfNeeded() {
      DataFetcher$SuccessfulComputationResult<T> lastResult = this.task.lastResult;
      if (lastResult != null && this.lastCheckTime < lastResult.time) {
         this.output.accept(lastResult.value);
         this.lastCheckTime = lastResult.time;
      }
   }

   private void runCallback() {
      DataFetcher$SuccessfulComputationResult<T> lastResult = this.task.lastResult;
      if (lastResult != null) {
         this.output.accept(lastResult.value);
         this.lastCheckTime = lastResult.time;
      }
   }

   private void reset() {
      this.task.reset();
      this.lastCheckTime = -1L;
   }
}
