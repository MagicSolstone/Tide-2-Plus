package net.minecraft.server.packs;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

record DownloadCacheCleaner$PathAndTime(Path path, FileTime modifiedTime) {
   public static final Comparator<DownloadCacheCleaner$PathAndTime> NEWEST_FIRST = Comparator.comparing(DownloadCacheCleaner$PathAndTime::modifiedTime)
      .reversed();
}
