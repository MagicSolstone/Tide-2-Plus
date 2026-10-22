package net.minecraft.util.filefix.virtualfilesystem;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

class CopyOnWriteFSProvider$2 implements DirectoryStream<Path> {
   CopyOnWriteFSProvider$2(final CopyOnWriteFSProvider this$0, final List val$result) {
      this.val$result = val$resultx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void close() {
   }

   @Override
   public Iterator<Path> iterator() {
      return this.val$result.iterator();
   }
}
