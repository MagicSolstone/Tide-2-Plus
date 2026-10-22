package net.minecraft.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.hash.HashCode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import org.jspecify.annotations.Nullable;

record HashCache$ProviderCache(String version, ImmutableMap<Path, HashCode> data) {
   @Nullable
   public HashCode get(final Path path) {
      return (HashCode)this.data.get(path);
   }

   public int count() {
      return this.data.size();
   }

   public static HashCache$ProviderCache load(final Path rootDir, final Path cacheFile) throws IOException {
      HashCache$ProviderCache var7;
      try (BufferedReader reader = Files.newBufferedReader(cacheFile, StandardCharsets.UTF_8)) {
         String header = reader.readLine();
         if (!header.startsWith("// ")) {
            throw new IllegalStateException("Missing cache file header");
         }

         String[] headerFields = header.substring("// ".length()).split("\t", 2);
         String savedVersionId = headerFields[0];
         Builder<Path, HashCode> result = ImmutableMap.builder();
         reader.lines().forEach(s -> {
            int i = s.indexOf(32);
            result.put(rootDir.resolve(s.substring(i + 1)), HashCode.fromString(s.substring(0, i)));
         });
         var7 = new HashCache$ProviderCache(savedVersionId, result.build());
      }

      return var7;
   }

   public void save(final Path rootDir, final Path cacheFile, final String extraHeaderInfo) {
      try (BufferedWriter output = Files.newBufferedWriter(cacheFile, StandardCharsets.UTF_8)) {
         output.write("// ");
         output.write(this.version);
         output.write(9);
         output.write(extraHeaderInfo);
         output.newLine();
         UnmodifiableIterator var5 = this.data.entrySet().iterator();

         while (var5.hasNext()) {
            Entry<Path, HashCode> e = (Entry<Path, HashCode>)var5.next();
            output.write(e.getValue().toString());
            output.write(32);
            output.write(rootDir.relativize(e.getKey()).toString());
            output.newLine();
         }
      } catch (IOException var9) {
         HashCache.LOGGER.warn("Unable write cachefile {}", cacheFile, var9);
      }
   }
}
