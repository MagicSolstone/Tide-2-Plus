package net.minecraft.util.thread;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import net.minecraft.util.Util;

public class ParallelMapTransform {
   private static final int DEFAULT_TASKS_PER_THREAD = 16;

   public static <K, U, V> CompletableFuture<Map<K, V>> schedule(
      final Map<K, U> input, final BiFunction<K, U, V> operation, final int maxTaskCount, final Executor executor
   ) {
      int inputSize = input.size();
      if (inputSize == 0) {
         return CompletableFuture.completedFuture(Map.of());
      } else if (inputSize == 1) {
         Entry<K, U> element = input.entrySet().iterator().next();
         K key = element.getKey();
         U value = element.getValue();
         return CompletableFuture.supplyAsync(() -> {
            V result = operation.apply(key, value);
            return result != null ? Map.of(key, result) : Map.of();
         }, executor);
      } else {
         ParallelMapTransform$SplitterBase<K, U, V> splitter = (ParallelMapTransform$SplitterBase<K, U, V>)(inputSize <= maxTaskCount
            ? new ParallelMapTransform$SingleTaskSplitter<>(operation, inputSize)
            : new ParallelMapTransform$BatchedTaskSplitter<>(operation, inputSize, maxTaskCount));
         return splitter.scheduleTasks(input, executor);
      }
   }

   public static <K, U, V> CompletableFuture<Map<K, V>> schedule(final Map<K, U> input, final BiFunction<K, U, V> operation, final Executor executor) {
      int maxTaskCount = Util.maxAllowedExecutorThreads() * 16;
      return schedule(input, operation, maxTaskCount, executor);
   }
}
