package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@FunctionalInterface
public interface SimpleReloadInstance$StateFactory<S> {
   SimpleReloadInstance$StateFactory<Void> SIMPLE = (currentReload, previousStep, listener, taskExecutor, reloadExecutor) -> listener.reload(
      currentReload, taskExecutor, previousStep, reloadExecutor
   );

   CompletableFuture<S> create(
      PreparableReloadListener$SharedState sharedState,
      PreparableReloadListener$PreparationBarrier previousStep,
      PreparableReloadListener listener,
      Executor taskExecutor,
      Executor reloadExecutor
   );
}
