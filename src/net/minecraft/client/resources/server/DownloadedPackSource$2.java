package net.minecraft.client.resources.server;

import java.util.Objects;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class DownloadedPackSource$2 implements PackLoadFeedback {
   DownloadedPackSource$2(final DownloadedPackSource this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void reportUpdate(final UUID id, final PackLoadFeedback$Update result) {
      this.this$0.packFeedback.reportUpdate(id, result);
   }

   @Override
   public void reportFinalResult(final UUID id, final PackLoadFeedback$FinalResult result) {
      this.this$0.packFeedback.reportFinalResult(id, result);
   }
}
