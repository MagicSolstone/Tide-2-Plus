package net.minecraft.client.resources.server;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class DownloadedPackSource$7 implements PackLoadFeedback {
   DownloadedPackSource$7(final DownloadedPackSource this$0, final PackLoadFeedback val$original, final UUID val$packId, final CompletableFuture val$result) {
      this.val$original = val$originalx;
      this.val$packId = val$packIdx;
      this.val$result = val$resultx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void reportUpdate(final UUID id, final PackLoadFeedback$Update result) {
      this.val$original.reportUpdate(id, result);
   }

   @Override
   public void reportFinalResult(final UUID id, final PackLoadFeedback$FinalResult status) {
      if (this.val$packId.equals(id)) {
         this.this$0.packFeedback = this.val$original;
         if (status == PackLoadFeedback$FinalResult.APPLIED) {
            this.val$result.complete(null);
         } else {
            this.val$result.completeExceptionally(new IllegalStateException("Failed to apply pack " + id + ", reason: " + status));
         }
      }

      this.val$original.reportFinalResult(id, status);
   }
}
