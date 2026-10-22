package net.minecraft.server.packs;

import java.nio.file.Path;
import java.util.Comparator;

record DownloadCacheCleaner$PathAndPriority(Path path, int removalPriority) {
   public static final Comparator<DownloadCacheCleaner$PathAndPriority> HIGHEST_PRIORITY_FIRST = Comparator.comparing(
         DownloadCacheCleaner$PathAndPriority::removalPriority
      )
      .reversed();
}
