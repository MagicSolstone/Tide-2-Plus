package net.minecraft.client.resources.server;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mojang.util.UndashedUuid;
import java.net.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.client.User;
import net.minecraft.server.packs.DownloadQueue;
import net.minecraft.server.packs.DownloadQueue$BatchConfig;
import net.minecraft.server.packs.DownloadQueue$BatchResult;
import net.minecraft.server.packs.DownloadQueue$DownloadRequest;
import net.minecraft.server.packs.PackType;

@Environment(EnvType.CLIENT)
class DownloadedPackSource$4 implements PackDownloader {
   private static final int MAX_PACK_SIZE_BYTES = 262144000;
   private static final HashFunction CACHE_HASHING_FUNCTION = Hashing.sha1();

   DownloadedPackSource$4(
      final DownloadedPackSource this$0,
      final User val$user,
      final DownloadQueue val$downloadQueue,
      final Proxy val$proxy,
      final Executor val$mainThreadExecutor
   ) {
      this.val$user = val$userx;
      this.val$downloadQueue = val$downloadQueuex;
      this.val$proxy = val$proxyx;
      this.val$mainThreadExecutor = val$mainThreadExecutorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   private Map<String, String> createDownloadHeaders() {
      WorldVersion version = SharedConstants.getCurrentVersion();
      return Map.of(
         "X-Minecraft-Username",
         this.val$user.getName(),
         "X-Minecraft-UUID",
         UndashedUuid.toString(this.val$user.getProfileId()),
         "X-Minecraft-Version",
         version.name(),
         "X-Minecraft-Version-ID",
         version.id(),
         "X-Minecraft-Pack-Format",
         String.valueOf(version.packVersion(PackType.CLIENT_RESOURCES)),
         "User-Agent",
         "Minecraft Java/" + version.name()
      );
   }

   @Override
   public void download(final Map<UUID, DownloadQueue$DownloadRequest> requests, final Consumer<DownloadQueue$BatchResult> output) {
      this.val$downloadQueue
         .downloadBatch(
            new DownloadQueue$BatchConfig(
               CACHE_HASHING_FUNCTION, 262144000, this.createDownloadHeaders(), this.val$proxy, this.this$0.createDownloadNotifier(requests.size())
            ),
            requests
         )
         .thenAcceptAsync(output, this.val$mainThreadExecutor);
   }
}
