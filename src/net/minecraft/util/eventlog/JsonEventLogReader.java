package net.minecraft.util.eventlog;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import org.jspecify.annotations.Nullable;

public interface JsonEventLogReader<T> extends Closeable {
   static <T> JsonEventLogReader<T> create(final Codec<T> codec, final Reader reader) {
      JsonReader jsonReader = new JsonReader(reader);
      jsonReader.setStrictness(Strictness.LENIENT);
      return new JsonEventLogReader$1(jsonReader, codec);
   }

   @Nullable
   T next() throws IOException;
}
