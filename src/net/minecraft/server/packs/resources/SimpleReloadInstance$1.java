package net.minecraft.server.packs.resources;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.Unit;

class SimpleReloadInstance$1 implements PreparableReloadListener$PreparationBarrier {
   SimpleReloadInstance$1(
      final SimpleReloadInstance this$0,
      final Executor val$mainThreadExecutor,
      final PreparableReloadListener val$listener,
      final CompletableFuture val$previousBarrier
   ) {
      this.val$mainThreadExecutor = val$mainThreadExecutorx;
      this.val$listener = val$listenerx;
      this.val$previousBarrier = val$previousBarrierx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> CompletableFuture<T> wait(final T t) {
      this.val$mainThreadExecutor.execute(() -> {
         this.this$0.preparingListeners.remove(listener);
         if (this.this$0.preparingListeners.isEmpty()) {
            this.this$0.allPreparations.complete(Unit.INSTANCE);
         }
      });
      return this.this$0.allPreparations.thenCombine(this.val$previousBarrier, (v1, v2) -> t);
   }
}
