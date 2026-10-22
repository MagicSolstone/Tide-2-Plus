package net.minecraft.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

class TaskChainer$1 implements TaskChainer {
   TaskChainer$1(final Executor val$executor) {
      this.val$executor = val$executorx;
   }

   @Override
   public <T> void append(final CompletableFuture<T> preparation, final Consumer<T> chainedTask) {
      preparation.thenAcceptAsync(chainedTask, this.val$executor).exceptionally(e -> {
         LOGGER.error("Task failed", e);
         return null;
      });
   }
}
