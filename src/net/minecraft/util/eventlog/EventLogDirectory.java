package net.minecraft.util.eventlog;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class EventLogDirectory {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int COMPRESS_BUFFER_SIZE = 4096;
   private static final String COMPRESSED_EXTENSION = ".gz";
   private final Path root;
   private final String extension;

   private EventLogDirectory(final Path root, final String extension) {
      this.root = root;
      this.extension = extension;
   }

   public static EventLogDirectory open(final Path root, final String extension) throws IOException {
      Files.createDirectories(root);
      return new EventLogDirectory(root, extension);
   }

   public EventLogDirectory$FileList listFiles() throws IOException {
      EventLogDirectory$FileList var2;
      try (Stream<Path> list = Files.list(this.root)) {
         var2 = new EventLogDirectory$FileList(list.filter(x$0 -> Files.isRegularFile(x$0)).map(this::parseFile).filter(Objects::nonNull).toList());
      }

      return var2;
   }

   @Nullable
   private EventLogDirectory$File parseFile(final Path path) {
      String fileName = path.getFileName().toString();
      int extensionIndex = fileName.indexOf(46);
      if (extensionIndex == -1) {
         return null;
      } else {
         EventLogDirectory$FileId id = EventLogDirectory$FileId.parse(fileName.substring(0, extensionIndex));
         if (id != null) {
            String extension = fileName.substring(extensionIndex);
            if (extension.equals(this.extension)) {
               return new EventLogDirectory$RawFile(path, id);
            }

            if (extension.equals(this.extension + ".gz")) {
               return new EventLogDirectory$CompressedFile(path, id);
            }
         }

         return null;
      }
   }

   private static void tryCompress(final Path raw, final Path compressed) throws IOException {
      if (Files.exists(compressed)) {
         throw new IOException("Compressed target file already exists: " + compressed);
      } else {
         try (FileChannel channel = FileChannel.open(raw, StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            FileLock lock = channel.tryLock();
            if (lock == null) {
               throw new IOException("Raw log file is already locked, cannot compress: " + raw);
            }

            writeCompressed(channel, compressed);
            channel.truncate(0L);
         }

         Files.delete(raw);
      }
   }

   private static void writeCompressed(final ReadableByteChannel channel, final Path target) throws IOException {
      try (OutputStream output = new GZIPOutputStream(Files.newOutputStream(target))) {
         byte[] bytes = new byte[4096];
         ByteBuffer buffer = ByteBuffer.wrap(bytes);

         while (channel.read(buffer) >= 0) {
            buffer.flip();
            output.write(bytes, 0, buffer.limit());
            buffer.clear();
         }
      }
   }

   public EventLogDirectory$RawFile createNewFile(final LocalDate date) throws IOException {
      int index = 1;
      Set<EventLogDirectory$FileId> files = this.listFiles().ids();

      EventLogDirectory$FileId id;
      do {
         id = new EventLogDirectory$FileId(date, index++);
      } while (files.contains(id));

      EventLogDirectory$RawFile file = new EventLogDirectory$RawFile(this.root.resolve(id.toFileName(this.extension)), id);
      Files.createFile(file.path());
      return file;
   }
}
