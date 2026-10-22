package net.minecraft.util.eventlog;

import java.io.IOException;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.jspecify.annotations.Nullable;

public record EventLogDirectory$RawFile(Path path, EventLogDirectory$FileId id) implements EventLogDirectory$File {
   public FileChannel openChannel() throws IOException {
      return FileChannel.open(this.path, StandardOpenOption.WRITE, StandardOpenOption.READ);
   }

   @Nullable
   @Override
   public Reader openReader() throws IOException {
      return Files.exists(this.path) ? Files.newBufferedReader(this.path) : null;
   }

   @Override
   public EventLogDirectory$CompressedFile compress() throws IOException {
      Path compressedPath = this.path.resolveSibling(this.path.getFileName().toString() + ".gz");
      EventLogDirectory.tryCompress(this.path, compressedPath);
      return new EventLogDirectory$CompressedFile(compressedPath, this.id);
   }
}
