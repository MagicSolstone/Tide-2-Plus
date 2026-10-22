package com.mojang.realmsclient.gui.task;

import com.mojang.datafixers.util.Either;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DataFetcher$Task<T> {
   private final String id;
   private final Callable<T> updater;
   private final long period;
   private final RepeatedDelayStrategy repeatStrategy;
   @Nullable
   private CompletableFuture<DataFetcher$ComputationResult<T>> pendingTask;
   @Nullable
   private DataFetcher$SuccessfulComputationResult<T> lastResult;
   private long nextUpdate;

   private DataFetcher$Task(
      final String this$0, final Callable<T> id, final long updater, final RepeatedDelayStrategy period, final RepeatedDelayStrategy repeatStrategy
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.nextUpdate = -1L;
      this.id = id;
      this.updater = updater;
      this.period = period;
      this.repeatStrategy = repeatStrategy;
   }

   private void updateIfNeeded(final long currentTime) {
      if (this.pendingTask != null) {
         DataFetcher$ComputationResult<T> result = this.pendingTask.getNow(null);
         if (result == null) {
            return;
         }

         this.pendingTask = null;
         long completionTime = result.time;
         result.value().ifLeft(value -> {
            this.lastResult = new DataFetcher$SuccessfulComputationResult<>((T)value, completionTime);
            this.nextUpdate = completionTime + this.period * this.repeatStrategy.delayCyclesAfterSuccess();
         }).ifRight(e -> {
            long cycles = this.repeatStrategy.delayCyclesAfterFailure();
            DataFetcher.LOGGER.warn("Failed to process task {}, will repeat after {} cycles", new Object[]{this.id, cycles, e});
            this.nextUpdate = completionTime + this.period * cycles;
         });
      }

      if (this.nextUpdate <= currentTime) {
         this.pendingTask = CompletableFuture.supplyAsync(() -> {
            try {
               T resultx = this.updater.call();
               long completionTimex = this.this$0.timeSource.get(this.this$0.resolution);
               return new DataFetcher$ComputationResult<>(Either.left(resultx), completionTimex);
            } catch (Exception var4x) {
               long completionTimex = this.this$0.timeSource.get(this.this$0.resolution);
               return new DataFetcher$ComputationResult<>(Either.right(var4x), completionTimex);
            }
         }, this.this$0.executor);
      }
   }

   public void reset() {
      this.pendingTask = null;
      this.lastResult = null;
      this.nextUpdate = -1L;
   }
}
