package net.minecraft.server.packs;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record DownloadQueue$BatchResult(Map<UUID, Path> downloaded, Set<UUID> failed) {
   public DownloadQueue$BatchResult() {
      this(new HashMap<>(), new HashSet<>());
   }
}
