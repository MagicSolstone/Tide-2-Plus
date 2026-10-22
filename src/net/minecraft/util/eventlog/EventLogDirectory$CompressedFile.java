package net.minecraft.util.eventlog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import org.jspecify.annotations.Nullable;

public record EventLogDirectory$CompressedFile(Path path, EventLogDirectory$FileId id) implements EventLogDirectory$File {
   @Nullable
   @Override
   public Reader openReader() throws IOException {
      return !Files.exists(this.path)
         ? null
         : new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(this.path)), StandardCharsets.UTF_8));
   }

   @Override
   public EventLogDirectory$CompressedFile compress() {
      return this;
   }
}
