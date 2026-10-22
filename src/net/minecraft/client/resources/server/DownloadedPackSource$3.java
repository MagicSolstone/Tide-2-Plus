package net.minecraft.client.resources.server;

import com.mojang.realmsclient.Unit;
import java.util.Objects;
import java.util.OptionalLong;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.SystemToast$SystemToastId;
import net.minecraft.network.chat.Component;
import net.minecraft.util.HttpUtil$DownloadProgressListener;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class DownloadedPackSource$3 implements HttpUtil$DownloadProgressListener {
   private final SystemToast$SystemToastId toastId;
   private Component title;
   @Nullable
   private Component message;
   private int count;
   private int failCount;
   private OptionalLong totalBytes;

   DownloadedPackSource$3(final DownloadedPackSource this$0, final int val$totalCount) {
      this.val$totalCount = val$totalCountx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.toastId = new SystemToast$SystemToastId();
      this.title = Component.empty();
      this.message = null;
      this.totalBytes = OptionalLong.empty();
   }

   private void updateToast() {
      this.this$0.minecraft.execute(() -> SystemToast.addOrUpdate(this.this$0.minecraft.gui.toastManager(), this.toastId, this.title, this.message));
   }

   private void updateProgress(final long bytesSoFar) {
      if (this.totalBytes.isPresent()) {
         this.message = Component.translatable("download.pack.progress.percent", bytesSoFar * 100L / this.totalBytes.getAsLong());
      } else {
         this.message = Component.translatable("download.pack.progress.bytes", Unit.humanReadable(bytesSoFar));
      }

      this.updateToast();
   }

   @Override
   public void requestStart() {
      this.count++;
      this.title = Component.translatable("download.pack.title", this.count, this.val$totalCount);
      this.updateToast();
      DownloadedPackSource.LOGGER.debug("Starting pack {}/{} download", this.count, this.val$totalCount);
   }

   @Override
   public void downloadStart(final OptionalLong sizeBytes) {
      DownloadedPackSource.LOGGER.debug("File size = {} bytes", sizeBytes);
      this.totalBytes = sizeBytes;
      this.updateProgress(0L);
   }

   @Override
   public void downloadedBytes(final long bytesSoFar) {
      DownloadedPackSource.LOGGER.debug("Progress for pack {}: {} bytes", this.count, bytesSoFar);
      this.updateProgress(bytesSoFar);
   }

   @Override
   public void requestFinished(final boolean success) {
      if (!success) {
         DownloadedPackSource.LOGGER.info("Pack {} failed to download", this.count);
         this.failCount++;
      } else {
         DownloadedPackSource.LOGGER.debug("Download ended for pack {}", this.count);
      }

      if (this.count == this.val$totalCount) {
         if (this.failCount > 0) {
            this.title = Component.translatable("download.pack.failed", this.failCount, this.val$totalCount);
            this.message = null;
            this.updateToast();
         } else {
            SystemToast.forceHide(this.this$0.minecraft.gui.toastManager(), this.toastId);
         }
      }
   }
}
