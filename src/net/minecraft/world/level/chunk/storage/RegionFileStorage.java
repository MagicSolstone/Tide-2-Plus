package net.minecraft.world.level.chunk.storage;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.util.ExceptionCollector;
import net.minecraft.util.FileUtil;
import net.minecraft.world.level.ChunkPos;
import org.jspecify.annotations.Nullable;

public final class RegionFileStorage implements AutoCloseable {
   public static final String ANVIL_EXTENSION = ".mca";
   private static final int MAX_CACHE_SIZE = 256;
   private final Long2ObjectLinkedOpenHashMap<Optional<RegionFile>> regionCache = new Long2ObjectLinkedOpenHashMap();
   private final RegionStorageInfo info;
   private final Path folder;
   private final boolean sync;

   public RegionFileStorage(final RegionStorageInfo info, final Path folder, final boolean sync) {
      this.folder = folder;
      this.sync = sync;
      this.info = info;
   }

   private Path regionPath(final ChunkPos pos) {
      return this.folder.resolve("r." + pos.getRegionX() + "." + pos.getRegionZ() + ".mca");
   }

   @Nullable
   private RegionFile getRegionFile(final ChunkPos pos, final boolean create) throws IOException {
      long key = ChunkPos.pack(pos.getRegionX(), pos.getRegionZ());
      Optional<RegionFile> cached = (Optional<RegionFile>)this.regionCache.getAndMoveToFirst(key);
      if (cached != null) {
         if (cached.isPresent()) {
            return cached.get();
         }

         if (!create) {
            return null;
         }
      }

      Path path = this.regionPath(pos);
      if (!create && !Files.isRegularFile(path)) {
         this.cache(key, Optional.empty());
         return null;
      } else {
         FileUtil.createDirectoriesSafe(this.folder);
         RegionFile newRegion = new RegionFile(this.info, path, this.folder, this.sync);
         this.cache(key, Optional.of(newRegion));
         return newRegion;
      }
   }

   private RegionFile getOrCreateRegionFile(final ChunkPos pos) throws IOException {
      return Objects.requireNonNull(this.getRegionFile(pos, true));
   }

   private void cache(final long key, final Optional<RegionFile> entry) throws IOException {
      this.regionCache.putAndMoveToFirst(key, entry);
      if (this.regionCache.size() > 256) {
         Optional<RegionFile> evicted = (Optional<RegionFile>)this.regionCache.removeLast();
         if (evicted.isPresent()) {
            evicted.get().close();
         }
      }
   }

   @Nullable
   public CompoundTag read(final ChunkPos pos) throws IOException {
      RegionFile region = this.getRegionFile(pos, false);
      if (region == null) {
         return null;
      } else {
         CompoundTag var4;
         try (DataInputStream regionChunkInputStream = region.getChunkDataInputStream(pos)) {
            if (regionChunkInputStream == null) {
               return null;
            }

            var4 = NbtIo.read(regionChunkInputStream);
         }

         return var4;
      }
   }

   public void scanChunk(final ChunkPos pos, final StreamTagVisitor scanner) throws IOException {
      RegionFile region = this.getRegionFile(pos, false);
      if (region != null) {
         try (DataInputStream regionChunkInputStream = region.getChunkDataInputStream(pos)) {
            if (regionChunkInputStream != null) {
               NbtIo.parse(regionChunkInputStream, scanner, NbtAccounter.unlimitedHeap());
            }
         }
      }
   }

   public void write(final ChunkPos pos, @Nullable final CompoundTag value) throws IOException {
      if (!SharedConstants.DEBUG_DONT_SAVE_WORLD) {
         RegionFile region = this.getOrCreateRegionFile(pos);
         if (value == null) {
            region.clear(pos);
         } else {
            try (DataOutputStream output = region.getChunkDataOutputStream(pos)) {
               NbtIo.write(value, output);
            }
         }
      }
   }

   @Override
   public void close() throws IOException {
      ExceptionCollector<IOException> exception = new ExceptionCollector<>();
      ObjectIterator var2 = this.regionCache.values().iterator();

      while (var2.hasNext()) {
         Optional<RegionFile> entry = (Optional<RegionFile>)var2.next();
         if (!entry.isEmpty()) {
            try {
               entry.get().close();
            } catch (IOException var5) {
               exception.add(var5);
            }
         }
      }

      exception.throwIfPresent();
   }

   public void flush() throws IOException {
      ObjectIterator var1 = this.regionCache.values().iterator();

      while (var1.hasNext()) {
         Optional<RegionFile> entry = (Optional<RegionFile>)var1.next();
         if (entry.isPresent()) {
            entry.get().flush();
         }
      }
   }

   public RegionStorageInfo info() {
      return this.info;
   }
}
