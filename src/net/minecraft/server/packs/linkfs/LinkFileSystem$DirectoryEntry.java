package net.minecraft.server.packs.linkfs;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

record LinkFileSystem$DirectoryEntry(Map<String, LinkFileSystem$DirectoryEntry> children, Map<String, Path> files) {
   public LinkFileSystem$DirectoryEntry() {
      this(new HashMap<>(), new HashMap<>());
   }
}
