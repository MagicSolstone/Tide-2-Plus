package net.minecraft.util.thread;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

abstract class ParallelMapTransform$SplitterBase<K, U, V> {
   private int lastScheduledIndex;
   private int currentIndex;
   private final CompletableFuture<?>[] tasks;
   private int batchIndex;
   private final ParallelMapTransform$Container<K, U, V> container;

   private ParallelMapTransform$SplitterBase(final BiFunction<K, U, V> operation, final int size, final int taskCount) {
      this.container = new ParallelMapTransform$Container<>(operation, size);
      this.tasks = new CompletableFuture[taskCount];
   }

   private int pendingBatchSize() {
      return this.currentIndex - this.lastScheduledIndex;
   }

   public CompletableFuture<Map<K, V>> scheduleTasks(final Map<K, U> input, final Executor executor) {
      input.forEach((key, inputValue) -> {
         this.container.put(this.currentIndex++, (K)key, (U)inputValue);
         if (this.pendingBatchSize() == this.batchSize(this.batchIndex)) {
            this.tasks[this.batchIndex++] = this.scheduleBatch(this.container, this.lastScheduledIndex, this.currentIndex, executor);
            this.lastScheduledIndex = this.currentIndex;
         }
      });
      if (!$assertionsDisabled && this.currentIndex != this.container.size()) {
         throw new AssertionError();
      } else if (!$assertionsDisabled && this.lastScheduledIndex != this.currentIndex) {
         throw new AssertionError();
      } else if (!$assertionsDisabled && this.batchIndex != this.tasks.length) {
         throw new AssertionError();
      } else {
         return this.scheduleFinalOperation(CompletableFuture.allOf(this.tasks), this.container);
      }
   }

   protected abstract int batchSize(int index);

   protected abstract CompletableFuture<?> scheduleBatch(ParallelMapTransform$Container<K, U, V> container, int startIndex, int endIndex, Executor executor);

   protected abstract CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform$Container<K, U, V> container);
}
