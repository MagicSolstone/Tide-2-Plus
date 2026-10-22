package net.minecraft.util.eventlog;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.io.EOFException;
import java.io.IOException;
import org.jspecify.annotations.Nullable;

class JsonEventLogReader$1 implements JsonEventLogReader<T> {
   JsonEventLogReader$1(final JsonReader val$jsonReader, final Codec val$codec) {
      this.val$jsonReader = val$jsonReaderx;
      this.val$codec = val$codecx;
   }

   @Nullable
   @Override
   public T next() throws IOException {
      try {
         if (!this.val$jsonReader.hasNext()) {
            return null;
         } else {
            JsonElement json = JsonParser.parseReader(this.val$jsonReader);
            return (T)this.val$codec.parse(JsonOps.INSTANCE, json).getOrThrow(IOException::new);
         }
      } catch (JsonParseException var2) {
         throw new IOException(var2);
      } catch (EOFException var3) {
         return null;
      }
   }

   @Override
   public void close() throws IOException {
      this.val$jsonReader.close();
   }
}
