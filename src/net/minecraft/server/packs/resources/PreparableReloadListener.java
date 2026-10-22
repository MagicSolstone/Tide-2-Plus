package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@FunctionalInterface
public interface PreparableReloadListener {
   CompletableFuture<Void> reload(
      PreparableReloadListener$SharedState currentReload,
      Executor taskExecutor,
      PreparableReloadListener$PreparationBarrier preparationBarrier,
      Executor reloadExecutor
   );

   default void prepareSharedState(final PreparableReloadListener$SharedState currentReload) {
   }

   default String getName() {
      return this.getClass().getSimpleName();
   }
}
