package net.minecraft.util.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

class ParallelMapTransform$SingleTaskSplitter<K, U, V> extends ParallelMapTransform$SplitterBase<K, U, V> {
   private ParallelMapTransform$SingleTaskSplitter(final BiFunction<K, U, V> operation, final int size) {
      super(operation, size, size);
   }

   @Override
   protected int batchSize(final int index) {
      return 1;
   }

   @Override
   protected CompletableFuture<?> scheduleBatch(
      final ParallelMapTransform$Container<K, U, V> container, final int startIndex, final int endIndex, final Executor executor
   ) {
      if (!$assertionsDisabled && startIndex + 1 != endIndex) {
         throw new AssertionError();
      } else {
         return CompletableFuture.runAsync(() -> container.applyOperation(startIndex), executor);
      }
   }

   @Override
   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(
      final CompletableFuture<?> allTasksDone, final ParallelMapTransform$Container<K, U, V> container
   ) {
      return allTasksDone.thenApply(ignored -> {
         Map<K, V> result = new HashMap<>(container.size());

         for (int i = 0; i < container.size(); i++) {
            container.copyOut(i, result);
         }

         return result;
      });
   }
}
