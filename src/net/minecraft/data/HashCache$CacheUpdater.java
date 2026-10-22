package net.minecraft.data;

import com.google.common.hash.HashCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

class HashCache$CacheUpdater implements CachedOutput {
   private final String provider;
   private final HashCache$ProviderCache oldCache;
   private final HashCache$ProviderCacheBuilder newCache;
   private final AtomicInteger writes = new AtomicInteger();
   private volatile boolean closed;

   private HashCache$CacheUpdater(final String provider, final String newVersionId, final HashCache$ProviderCache oldCache) {
      this.provider = provider;
      this.oldCache = oldCache;
      this.newCache = new HashCache$ProviderCacheBuilder(newVersionId);
   }

   private boolean shouldWrite(final Path path, final HashCode hash) {
      return !Objects.equals(this.oldCache.get(path), hash) || !Files.exists(path);
   }

   @Override
   public void writeIfNeeded(final Path path, final byte[] input, final HashCode hash) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("Cannot write to cache as it has already been closed");
      } else {
         if (this.shouldWrite(path, hash)) {
            this.writes.incrementAndGet();
            Files.createDirectories(path.getParent());
            Files.write(path, input);
         }

         this.newCache.put(path, hash);
      }
   }

   public HashCache$UpdateResult close() {
      this.closed = true;
      return new HashCache$UpdateResult(this.provider, this.newCache.build(), this.writes.get());
   }
}
