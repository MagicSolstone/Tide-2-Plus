package net.minecraft.util;

import com.mojang.logging.LogUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import org.slf4j.Logger;

@FunctionalInterface
public interface TaskChainer {
   Logger LOGGER = LogUtils.getLogger();

   static TaskChainer immediate(final Executor executor) {
      return new TaskChainer$1(executor);
   }

   default void append(final Runnable task) {
      this.append(CompletableFuture.completedFuture(null), ignored -> task.run());
   }

   <T> void append(CompletableFuture<T> preparation, Consumer<T> chainedTask);
}
