package net.minecraft.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

record HashCache$ProviderCacheBuilder(String version, ConcurrentMap<Path, HashCode> data) {
   public HashCache$ProviderCacheBuilder(final String version) {
      this(version, new ConcurrentHashMap<>());
   }

   public void put(final Path path, final HashCode hash) {
      this.data.put(path, hash);
   }

   public HashCache$ProviderCache build() {
      return new HashCache$ProviderCache(this.version, ImmutableMap.copyOf(this.data));
   }
}
