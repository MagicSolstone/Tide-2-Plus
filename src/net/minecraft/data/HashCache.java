package net.minecraft.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.WorldVersion;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;

public class HashCache {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final String HEADER_MARKER = "// ";
   private final Path rootDir;
   private final Path cacheDir;
   private final String versionId;
   private final Map<String, HashCache$ProviderCache> caches;
   private final Set<String> cachesToWrite = new HashSet<>();
   private final Set<Path> cachePaths = new HashSet<>();
   private final int initialCount;
   private int writes;

   private Path getProviderCachePath(final String provider) {
      return this.cacheDir.resolve(Hashing.sha1().hashString(provider, StandardCharsets.UTF_8).toString());
   }

   public HashCache(final Path rootDir, final Collection<String> providerIds, final WorldVersion version) throws IOException {
      this.versionId = version.id();
      this.rootDir = rootDir;
      this.cacheDir = rootDir.resolve(".cache");
      Files.createDirectories(this.cacheDir);
      Map<String, HashCache$ProviderCache> loadedCaches = new HashMap<>();
      int initialCount = 0;

      for (String providerId : providerIds) {
         Path providerCachePath = this.getProviderCachePath(providerId);
         this.cachePaths.add(providerCachePath);
         HashCache$ProviderCache providerCache = readCache(rootDir, providerCachePath);
         loadedCaches.put(providerId, providerCache);
         initialCount += providerCache.count();
      }

      this.caches = loadedCaches;
      this.initialCount = initialCount;
   }

   private static HashCache$ProviderCache readCache(final Path rootDir, final Path providerCachePath) {
      if (Files.isReadable(providerCachePath)) {
         try {
            return HashCache$ProviderCache.load(rootDir, providerCachePath);
         } catch (Exception var3) {
            LOGGER.warn("Failed to parse cache {}, discarding", providerCachePath, var3);
         }
      }

      return new HashCache$ProviderCache("unknown", ImmutableMap.of());
   }

   public boolean shouldRunInThisVersion(final String providerId) {
      HashCache$ProviderCache result = this.caches.get(providerId);
      return result == null || !result.version.equals(this.versionId);
   }

   public CompletableFuture<HashCache$UpdateResult> generateUpdate(final String providerId, final HashCache$UpdateFunction function) {
      HashCache$ProviderCache existingCache = this.caches.get(providerId);
      if (existingCache == null) {
         throw new IllegalStateException("Provider not registered: " + providerId);
      } else {
         HashCache$CacheUpdater output = new HashCache$CacheUpdater(providerId, this.versionId, existingCache);
         return function.update(output).thenApply(unused -> output.close());
      }
   }

   public void applyUpdate(final HashCache$UpdateResult result) {
      this.caches.put(result.providerId(), result.cache());
      this.cachesToWrite.add(result.providerId());
      this.writes = this.writes + result.writes();
   }

   public void purgeStaleAndWrite() throws IOException {
      Set<Path> allowedFiles = new HashSet<>();
      this.caches.forEach((providerId, cache) -> {
         if (this.cachesToWrite.contains(providerId)) {
            Path cachePath = this.getProviderCachePath(providerId);
            cache.save(this.rootDir, cachePath, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(ZonedDateTime.now()) + "\t" + providerId);
         }

         allowedFiles.addAll(cache.data().keySet());
      });
      MutableInt found = new MutableInt();
      MutableInt removed = new MutableInt();
      Files.walkFileTree(this.rootDir, new HashCache$1(this, found, allowedFiles, removed));
      LOGGER.info(
         "Caching: total files: {}, old count: {}, new count: {}, removed stale: {}, written: {}",
         new Object[]{found, this.initialCount, allowedFiles.size(), removed, this.writes}
      );
   }
}
