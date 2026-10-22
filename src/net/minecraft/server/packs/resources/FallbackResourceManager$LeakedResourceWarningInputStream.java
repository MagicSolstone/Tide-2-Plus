package net.minecraft.server.packs.resources;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;
import net.minecraft.resources.Identifier;

class FallbackResourceManager$LeakedResourceWarningInputStream extends FilterInputStream {
   private final Supplier<String> message;
   private boolean closed;

   public FallbackResourceManager$LeakedResourceWarningInputStream(final InputStream wrapped, final Identifier location, final String name) {
      super(wrapped);
      Exception exception = new Exception("Stacktrace");
      this.message = () -> {
         StringWriter data = new StringWriter();
         exception.printStackTrace(new PrintWriter(data));
         return "Leaked resource: '" + location + "' loaded from pack: '" + name + "'\n" + data;
      };
   }

   @Override
   public void close() throws IOException {
      super.close();
      this.closed = true;
   }

   @Override
   protected void finalize() throws Throwable {
      if (!this.closed) {
         FallbackResourceManager.LOGGER.warn("{}", this.message.get());
      }

      super.finalize();
   }
}
