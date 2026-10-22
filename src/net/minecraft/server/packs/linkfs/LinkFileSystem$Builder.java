package net.minecraft.server.packs.linkfs;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

public class LinkFileSystem$Builder {
   private final LinkFileSystem$DirectoryEntry root = new LinkFileSystem$DirectoryEntry();

   public LinkFileSystem$Builder put(final List<String> path, final String name, final Path target) {
      LinkFileSystem$DirectoryEntry currentEntry = this.root;

      for (String segment : path) {
         currentEntry = currentEntry.children.computeIfAbsent(segment, n -> new LinkFileSystem$DirectoryEntry());
      }

      currentEntry.files.put(name, target);
      return this;
   }

   public LinkFileSystem$Builder put(final List<String> path, final Path target) {
      if (path.isEmpty()) {
         throw new IllegalArgumentException("Path can't be empty");
      } else {
         int lastIndex = path.size() - 1;
         return this.put(path.subList(0, lastIndex), path.get(lastIndex), target);
      }
   }

   public FileSystem build(final String name) {
      return new LinkFileSystem(name, this.root);
   }
}
