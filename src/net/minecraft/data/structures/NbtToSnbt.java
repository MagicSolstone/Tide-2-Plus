package net.minecraft.data.structures;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.FastBufferedInputStream;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class NbtToSnbt implements DataProvider {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Iterable<Path> inputFolders;
   private final PackOutput output;

   public NbtToSnbt(final PackOutput output, final Collection<Path> inputFolders) {
      this.inputFolders = inputFolders;
      this.output = output;
   }

   @Override
   public CompletableFuture<?> run(final CachedOutput cache) {
      Path output = this.output.getOutputFolder();
      List<CompletableFuture<?>> tasks = new ArrayList<>();

      for (Path input : this.inputFolders) {
         tasks.add(
            CompletableFuture.<CompletableFuture>supplyAsync(
                  () -> {
                     try {
                        CompletableFuture t$;
                        try (Stream<Path> walk = Files.walk(input)) {
                           t$ = CompletableFuture.allOf(
                              walk.filter(path -> path.toString().endsWith(".nbt"))
                                 .map(path -> CompletableFuture.runAsync(() -> convertStructure(cache, path, getName(input, path), output), Util.ioPool()))
                                 .toArray(CompletableFuture[]::new)
                           );
                        }

                        return t$;
                     } catch (IOException var8) {
                        LOGGER.error("Failed to read structure input directory", var8);
                        return CompletableFuture.completedFuture(null);
                     }
                  },
                  Util.backgroundExecutor().forName("NbtToSnbt")
               )
               .thenCompose(v -> v)
         );
      }

      return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
   }

   @Override
   public final String getName() {
      return "NBT -> SNBT";
   }

   private static String getName(final Path root, final Path path) {
      String name = root.relativize(path).toString().replaceAll("\\\\", "/");
      return name.substring(0, name.length() - ".nbt".length());
   }

   @Nullable
   public static Path convertStructure(final CachedOutput cache, final Path path, final String name, final Path output) {
      try {
         Path var8;
         try (
            InputStream rawInput = Files.newInputStream(path);
            InputStream input = new FastBufferedInputStream(rawInput);
         ) {
            Path resultPath = output.resolve(name + ".snbt");
            CompoundTag structure = NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap());
            writeSnbt(
               cache,
               resultPath,
               NbtUtils.structureToSnbt(
                  NbtUtils.addCurrentDataVersion(
                     DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), structure, NbtUtils.getDataVersion(structure))
                  )
               )
            );
            LOGGER.info("Converted {} from NBT to SNBT", name);
            var8 = resultPath;
         }

         return var8;
      } catch (IOException var13) {
         LOGGER.error("Couldn't convert {} from NBT to SNBT at {}", new Object[]{name, path, var13});
         return null;
      }
   }

   public static void writeSnbt(final CachedOutput cache, final Path destination, final String text) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      HashingOutputStream hashedBytes = new HashingOutputStream(Hashing.sha1(), bytes);
      hashedBytes.write(text.getBytes(StandardCharsets.UTF_8));
      hashedBytes.write(10);
      cache.writeIfNeeded(destination, bytes.toByteArray(), hashedBytes.hash());
   }
}
