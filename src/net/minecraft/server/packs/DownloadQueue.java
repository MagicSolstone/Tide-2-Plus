package net.minecraft.server.packs;

import com.google.common.hash.HashCode;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.util.FileUtil;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.util.eventlog.JsonEventLog;
import net.minecraft.util.thread.ConsecutiveExecutor;
import org.slf4j.Logger;

public class DownloadQueue implements AutoCloseable {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int MAX_KEPT_PACKS = 20;
   private final Path cacheDir;
   private final JsonEventLog<DownloadQueue$LogEntry> eventLog;
   private final ConsecutiveExecutor tasks = new ConsecutiveExecutor(Util.nonCriticalIoPool(), "download-queue");

   public DownloadQueue(final Path cacheDir) throws IOException {
      this.cacheDir = cacheDir;
      FileUtil.createDirectoriesSafe(cacheDir);
      this.eventLog = JsonEventLog.open(DownloadQueue$LogEntry.CODEC, cacheDir.resolve("log.json"));
      DownloadCacheCleaner.vacuumCacheDir(cacheDir, 20);
   }

   private DownloadQueue$BatchResult runDownload(final DownloadQueue$BatchConfig config, final Map<UUID, DownloadQueue$DownloadRequest> requests) {
      DownloadQueue$BatchResult result = new DownloadQueue$BatchResult();
      requests.forEach(
         (id, request) -> {
            Path targetDir = this.cacheDir.resolve(id.toString());
            Path downloadedFile = null;

            try {
               downloadedFile = HttpUtil.downloadFile(
                  targetDir, request.url, config.headers, config.hashFunction, request.hash, config.maxSize, config.proxy, config.listener
               );
               result.downloaded.put(id, downloadedFile);
            } catch (Exception var9) {
               LOGGER.error("Failed to download {}", request.url, var9);
               result.failed.add(id);
            }

            try {
               this.eventLog
                  .write(
                     new DownloadQueue$LogEntry(
                        id,
                        request.url.toString(),
                        Instant.now(),
                        Optional.ofNullable(request.hash).map(HashCode::toString),
                        downloadedFile != null ? this.getFileInfo(downloadedFile) : Either.left("download_failed")
                     )
                  );
            } catch (Exception var8) {
               LOGGER.error("Failed to log download of {}", request.url, var8);
            }
         }
      );
      return result;
   }

   private Either<String, DownloadQueue$FileInfoEntry> getFileInfo(final Path downloadedFile) {
      try {
         long size = Files.size(downloadedFile);
         Path relativePath = this.cacheDir.relativize(downloadedFile);
         return Either.right(new DownloadQueue$FileInfoEntry(relativePath.toString(), size));
      } catch (IOException var5) {
         LOGGER.error("Failed to get file size of {}", downloadedFile, var5);
         return Either.left("no_access");
      }
   }

   public CompletableFuture<DownloadQueue$BatchResult> downloadBatch(
      final DownloadQueue$BatchConfig config, final Map<UUID, DownloadQueue$DownloadRequest> requests
   ) {
      return CompletableFuture.supplyAsync(() -> this.runDownload(config, requests), this.tasks::schedule);
   }

   @Override
   public void close() throws IOException {
      this.tasks.close();
      this.eventLog.close();
   }
}
