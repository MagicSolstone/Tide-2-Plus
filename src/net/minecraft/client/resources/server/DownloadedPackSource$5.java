package net.minecraft.client.resources.server;

import java.util.Objects;
import java.util.concurrent.Executor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class DownloadedPackSource$5 implements Runnable {
   private boolean scheduledInMainExecutor;
   private boolean hasUpdates;

   DownloadedPackSource$5(final DownloadedPackSource this$0, final Executor val$mainThreadExecutor) {
      this.val$mainThreadExecutor = val$mainThreadExecutorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void run() {
      this.hasUpdates = true;
      if (!this.scheduledInMainExecutor) {
         this.scheduledInMainExecutor = true;
         this.val$mainThreadExecutor.execute(this::runAllUpdates);
      }
   }

   private void runAllUpdates() {
      while (this.hasUpdates) {
         this.hasUpdates = false;
         this.this$0.manager.tick();
      }

      this.scheduledInMainExecutor = false;
   }
}
