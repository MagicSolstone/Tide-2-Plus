package net.minecraft.util.eventlog;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import org.jspecify.annotations.Nullable;

public interface EventLogDirectory$File {
   Path path();

   EventLogDirectory$FileId id();

   @Nullable
   Reader openReader() throws IOException;

   EventLogDirectory$CompressedFile compress() throws IOException;
}
