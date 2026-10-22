package net.minecraft.data.structures;

import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class SnbtToNbt implements DataProvider {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final PackOutput output;
   private final Iterable<Path> inputFolders;
   private final List<SnbtToNbt$Filter> filters = Lists.newArrayList();

   public SnbtToNbt(final PackOutput output, final Path inputFolder) {
      this(output, List.of(inputFolder));
   }

   public SnbtToNbt(final PackOutput output, final Iterable<Path> inputFolders) {
      this.output = output;
      this.inputFolders = inputFolders;
   }

   public SnbtToNbt addFilter(final SnbtToNbt$Filter filter) {
      this.filters.add(filter);
      return this;
   }

   private CompoundTag applyFilters(final String name, final CompoundTag input) {
      CompoundTag result = input;

      for (SnbtToNbt$Filter filter : this.filters) {
         result = filter.apply(name, result);
      }

      return result;
   }

   @Override
   public CompletableFuture<?> run(final CachedOutput cache) {
      Path output = this.output.getOutputFolder();
      List<CompletableFuture<?>> tasks = Lists.newArrayList();

      for (Path input : this.inputFolders) {
         tasks.add(CompletableFuture.<CompletableFuture>supplyAsync(() -> {
            try {
               CompletableFuture t$;
               try (Stream<Path> files = Files.walk(input)) {
                  t$ = CompletableFuture.allOf(files.filter(path -> path.toString().endsWith(".snbt")).map(path -> CompletableFuture.runAsync(() -> {
                     SnbtToNbt$TaskResult structure = this.readStructure(path, this.getName(input, path));
                     this.storeStructureIfChanged(cache, structure, output);
                  }, Util.backgroundExecutor().forName("SnbtToNbt"))).toArray(CompletableFuture[]::new));
               }

               return t$;
            } catch (Exception var9) {
               throw new RuntimeException("Failed to read structure input directory, aborting", var9);
            }
         }, Util.backgroundExecutor().forName("SnbtToNbt")).thenCompose(v -> v));
      }

      return Util.sequenceFailFast(tasks);
   }

   @Override
   public final String getName() {
      return "SNBT -> NBT";
   }

   private String getName(final Path root, final Path path) {
      String name = root.relativize(path).toString().replaceAll("\\\\", "/");
      return name.substring(0, name.length() - ".snbt".length());
   }

   private SnbtToNbt$TaskResult readStructure(final Path path, final String name) {
      try {
         SnbtToNbt$TaskResult var10;
         try (BufferedReader reader = Files.newBufferedReader(path)) {
            String input = IOUtils.toString(reader);
            CompoundTag updated = this.applyFilters(name, NbtUtils.snbtToStructure(input));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            HashingOutputStream hos = new HashingOutputStream(Hashing.sha1(), bos);
            NbtIo.writeCompressed(updated, hos);
            byte[] bytes = bos.toByteArray();
            HashCode hash = hos.hash();
            var10 = new SnbtToNbt$TaskResult(name, bytes, hash);
         }

         return var10;
      } catch (Throwable var13) {
         throw new SnbtToNbt$StructureConversionException(path, var13);
      }
   }

   private void storeStructureIfChanged(final CachedOutput cache, final SnbtToNbt$TaskResult task, final Path output) {
      Path destination = output.resolve(task.name + ".nbt");

      try {
         cache.writeIfNeeded(destination, task.payload, task.hash);
      } catch (IOException var6) {
         LOGGER.error("Couldn't write structure {} at {}", new Object[]{task.name, destination, var6});
      }
   }
}
