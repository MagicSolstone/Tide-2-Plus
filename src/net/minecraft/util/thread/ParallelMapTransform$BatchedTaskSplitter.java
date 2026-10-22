package net.minecraft.util.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import net.minecraft.util.Mth;

class ParallelMapTransform$BatchedTaskSplitter<K, U, V> extends ParallelMapTransform$SplitterBase<K, U, V> {
   private final Map<K, V> result;
   private final int batchSize;
   private final int firstUndersizedBatchIndex;

   private ParallelMapTransform$BatchedTaskSplitter(final BiFunction<K, U, V> operation, final int size, final int maxTasks) {
      super(operation, size, maxTasks);
      this.result = new HashMap<>(size);
      this.batchSize = Mth.positiveCeilDiv(size, maxTasks);
      int fullCapacity = this.batchSize * maxTasks;
      int leftoverCapacity = fullCapacity - size;
      this.firstUndersizedBatchIndex = maxTasks - leftoverCapacity;
      if (!$assertionsDisabled && (this.firstUndersizedBatchIndex <= 0 || this.firstUndersizedBatchIndex > maxTasks)) {
         throw new AssertionError();
      }
   }

   @Override
   protected CompletableFuture<?> scheduleBatch(
      final ParallelMapTransform$Container<K, U, V> container, final int startIndex, final int endIndex, final Executor executor
   ) {
      int batchSize = endIndex - startIndex;
      if (!$assertionsDisabled && batchSize != this.batchSize && batchSize != this.batchSize - 1) {
         throw new AssertionError();
      } else {
         return CompletableFuture.runAsync(createTask(this.result, startIndex, endIndex, container), executor);
      }
   }

   @Override
   protected int batchSize(final int index) {
      return index < this.firstUndersizedBatchIndex ? this.batchSize : this.batchSize - 1;
   }

   private static <K, U, V> Runnable createTask(
      final Map<K, V> result, final int startIndex, final int endIndex, final ParallelMapTransform$Container<K, U, V> container
   ) {
      return () -> {
         for (int i = startIndex; i < endIndex; i++) {
            container.applyOperation(i);
         }

         synchronized (result) {
            for (int i = startIndex; i < endIndex; i++) {
               container.copyOut(i, result);
            }
         }
      };
   }

   @Override
   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(
      final CompletableFuture<?> allTasksDone, final ParallelMapTransform$Container<K, U, V> container
   ) {
      Map<K, V> result = this.result;
      return allTasksDone.thenApply(ignored -> result);
   }
}
