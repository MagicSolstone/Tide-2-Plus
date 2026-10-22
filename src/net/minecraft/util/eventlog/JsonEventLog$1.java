package net.minecraft.util.eventlog;

import java.io.IOException;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

class JsonEventLog$1 implements JsonEventLogReader<T> {
   private volatile long position;

   JsonEventLog$1(final JsonEventLog this$0, final JsonEventLogReader val$reader) {
      this.val$reader = val$readerx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Nullable
   @Override
   public T next() throws IOException {
      Object var1;
      try {
         this.this$0.channel.position(this.position);
         var1 = this.val$reader.next();
      } finally {
         this.position = this.this$0.channel.position();
      }

      return (T)var1;
   }

   @Override
   public void close() throws IOException {
      this.this$0.releaseReference();
   }
}
