package net.minecraft.client.resources.server;

import com.google.common.hash.HashCode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.DownloadQueue$BatchResult;
import net.minecraft.server.packs.DownloadQueue$DownloadRequest;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ServerPackManager {
   private final PackDownloader downloader;
   private final PackLoadFeedback packLoadFeedback;
   private final PackReloadConfig reloadConfig;
   private final Runnable updateRequest;
   private ServerPackManager$PackPromptStatus packPromptStatus;
   private final List<ServerPackManager$ServerPackData> packs = new ArrayList<>();

   public ServerPackManager(
      final PackDownloader downloader,
      final PackLoadFeedback packLoadFeedback,
      final PackReloadConfig reloadConfig,
      final Runnable updateRequest,
      final ServerPackManager$PackPromptStatus packPromptStatus
   ) {
      this.downloader = downloader;
      this.packLoadFeedback = packLoadFeedback;
      this.reloadConfig = reloadConfig;
      this.updateRequest = updateRequest;
      this.packPromptStatus = packPromptStatus;
   }

   private void registerForUpdate() {
      this.updateRequest.run();
   }

   private void markExistingPacksAsRemoved(final UUID id) {
      for (ServerPackManager$ServerPackData pack : this.packs) {
         if (pack.id.equals(id)) {
            pack.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.SERVER_REPLACED);
         }
      }
   }

   public void pushPack(final UUID id, final URL url, @Nullable final HashCode hash) {
      if (this.packPromptStatus == ServerPackManager$PackPromptStatus.DECLINED) {
         this.packLoadFeedback.reportFinalResult(id, PackLoadFeedback$FinalResult.DECLINED);
      } else {
         this.pushNewPack(id, new ServerPackManager$ServerPackData(id, url, hash));
      }
   }

   public void pushLocalPack(final UUID id, final Path path) {
      if (this.packPromptStatus == ServerPackManager$PackPromptStatus.DECLINED) {
         this.packLoadFeedback.reportFinalResult(id, PackLoadFeedback$FinalResult.DECLINED);
      } else {
         URL url;
         try {
            url = path.toUri().toURL();
         } catch (MalformedURLException var5) {
            throw new IllegalStateException("Can't convert path to URL " + path, var5);
         }

         ServerPackManager$ServerPackData pack = new ServerPackManager$ServerPackData(id, url, null);
         pack.downloadStatus = ServerPackManager$PackDownloadStatus.DONE;
         pack.path = path;
         this.pushNewPack(id, pack);
      }
   }

   private void pushNewPack(final UUID id, final ServerPackManager$ServerPackData pack) {
      this.markExistingPacksAsRemoved(id);
      this.packs.add(pack);
      if (this.packPromptStatus == ServerPackManager$PackPromptStatus.ALLOWED) {
         this.acceptPack(pack);
      }

      this.registerForUpdate();
   }

   private void acceptPack(final ServerPackManager$ServerPackData pack) {
      this.packLoadFeedback.reportUpdate(pack.id, PackLoadFeedback$Update.ACCEPTED);
      pack.promptAccepted = true;
   }

   @Nullable
   private ServerPackManager$ServerPackData findPackInfo(final UUID id) {
      for (ServerPackManager$ServerPackData pack : this.packs) {
         if (!pack.isRemoved() && pack.id.equals(id)) {
            return pack;
         }
      }

      return null;
   }

   public void popPack(final UUID id) {
      ServerPackManager$ServerPackData packInfo = this.findPackInfo(id);
      if (packInfo != null) {
         packInfo.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.SERVER_REMOVED);
         this.registerForUpdate();
      }
   }

   public void popAll() {
      for (ServerPackManager$ServerPackData pack : this.packs) {
         pack.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.SERVER_REMOVED);
      }

      this.registerForUpdate();
   }

   public void allowServerPacks() {
      this.packPromptStatus = ServerPackManager$PackPromptStatus.ALLOWED;

      for (ServerPackManager$ServerPackData pack : this.packs) {
         if (!pack.promptAccepted && !pack.isRemoved()) {
            this.acceptPack(pack);
         }
      }

      this.registerForUpdate();
   }

   public void rejectServerPacks() {
      this.packPromptStatus = ServerPackManager$PackPromptStatus.DECLINED;

      for (ServerPackManager$ServerPackData pack : this.packs) {
         if (!pack.promptAccepted) {
            pack.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.DECLINED);
         }
      }

      this.registerForUpdate();
   }

   public void resetPromptStatus() {
      this.packPromptStatus = ServerPackManager$PackPromptStatus.PENDING;
   }

   public void tick() {
      boolean downloadsPending = this.updateDownloads();
      if (!downloadsPending) {
         this.triggerReloadIfNeeded();
      }

      this.cleanupRemovedPacks();
   }

   private void cleanupRemovedPacks() {
      this.packs.removeIf(data -> {
         if (data.activationStatus != ServerPackManager$ActivationStatus.INACTIVE) {
            return false;
         } else if (data.removalReason != null) {
            PackLoadFeedback$FinalResult response = data.removalReason.serverResponse;
            if (response != null) {
               this.packLoadFeedback.reportFinalResult(data.id, response);
            }

            return true;
         } else {
            return false;
         }
      });
   }

   private void onDownload(final Collection<ServerPackManager$ServerPackData> data, final DownloadQueue$BatchResult result) {
      if (!result.failed().isEmpty()) {
         for (ServerPackManager$ServerPackData pack : this.packs) {
            if (pack.activationStatus != ServerPackManager$ActivationStatus.ACTIVE) {
               if (result.failed().contains(pack.id)) {
                  pack.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.DOWNLOAD_FAILED);
               } else {
                  pack.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.DISCARDED);
               }
            }
         }
      }

      for (ServerPackManager$ServerPackData packx : data) {
         Path packFile = result.downloaded().get(packx.id);
         if (packFile != null) {
            packx.downloadStatus = ServerPackManager$PackDownloadStatus.DONE;
            packx.path = packFile;
            if (!packx.isRemoved()) {
               this.packLoadFeedback.reportUpdate(packx.id, PackLoadFeedback$Update.DOWNLOADED);
            }
         }
      }

      this.registerForUpdate();
   }

   private boolean updateDownloads() {
      List<ServerPackManager$ServerPackData> downloadPacks = new ArrayList<>();
      boolean downloadsInProgress = false;

      for (ServerPackManager$ServerPackData pack : this.packs) {
         if (!pack.isRemoved() && pack.promptAccepted) {
            if (pack.downloadStatus != ServerPackManager$PackDownloadStatus.DONE) {
               downloadsInProgress = true;
            }

            if (pack.downloadStatus == ServerPackManager$PackDownloadStatus.REQUESTED) {
               pack.downloadStatus = ServerPackManager$PackDownloadStatus.PENDING;
               downloadPacks.add(pack);
            }
         }
      }

      if (!downloadPacks.isEmpty()) {
         Map<UUID, DownloadQueue$DownloadRequest> downloadRequests = new HashMap<>();

         for (ServerPackManager$ServerPackData packx : downloadPacks) {
            downloadRequests.put(packx.id, new DownloadQueue$DownloadRequest(packx.url, packx.hash));
         }

         this.downloader.download(downloadRequests, result -> this.onDownload(downloadPacks, result));
      }

      return downloadsInProgress;
   }

   private void triggerReloadIfNeeded() {
      boolean needsReload = false;
      List<ServerPackManager$ServerPackData> packsToLoad = new ArrayList<>();
      List<ServerPackManager$ServerPackData> packsToUnload = new ArrayList<>();

      for (ServerPackManager$ServerPackData pack : this.packs) {
         if (pack.activationStatus == ServerPackManager$ActivationStatus.PENDING) {
            return;
         }

         boolean shouldBeActive = pack.promptAccepted && pack.downloadStatus == ServerPackManager$PackDownloadStatus.DONE && !pack.isRemoved();
         if (shouldBeActive && pack.activationStatus == ServerPackManager$ActivationStatus.INACTIVE) {
            packsToLoad.add(pack);
            needsReload = true;
         }

         if (pack.activationStatus == ServerPackManager$ActivationStatus.ACTIVE) {
            if (!shouldBeActive) {
               needsReload = true;
               packsToUnload.add(pack);
            } else {
               packsToLoad.add(pack);
            }
         }
      }

      if (needsReload) {
         for (ServerPackManager$ServerPackData pack : packsToLoad) {
            if (pack.activationStatus != ServerPackManager$ActivationStatus.ACTIVE) {
               pack.activationStatus = ServerPackManager$ActivationStatus.PENDING;
            }
         }

         for (ServerPackManager$ServerPackData packx : packsToUnload) {
            packx.activationStatus = ServerPackManager$ActivationStatus.PENDING;
         }

         this.reloadConfig.scheduleReload(new ServerPackManager$1(this, packsToLoad, packsToUnload));
      }
   }
}
