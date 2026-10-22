package net.minecraft.server.packs.linkfs;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.DirectoryStream.Filter;
import java.util.Iterator;
import java.util.Objects;

class LinkFSProvider$1 implements DirectoryStream<Path> {
   LinkFSProvider$1(final LinkFSProvider this$0, final PathContents$DirectoryContents val$directoryContents, final Filter val$filter) {
      this.val$directoryContents = val$directoryContentsx;
      this.val$filter = val$filterx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public Iterator<Path> iterator() {
      return this.val$directoryContents.children().values().stream().filter(path -> {
         try {
            return filter.accept(path);
         } catch (IOException var3) {
            throw new DirectoryIteratorException(var3);
         }
      }).map(path -> (Path)path).iterator();
   }

   @Override
   public void close() {
   }
}
