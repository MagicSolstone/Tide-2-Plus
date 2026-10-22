package net.minecraft.server.packs;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;

public class DownloadCacheCleaner {
   private static final Logger LOGGER = LogUtils.getLogger();

   public static void vacuumCacheDir(final Path cacheDir, final int maxFiles) {
      try {
         List<DownloadCacheCleaner$PathAndTime> filesAndDates = listFilesWithModificationTimes(cacheDir);
         int toRemove = filesAndDates.size() - maxFiles;
         if (toRemove <= 0) {
            return;
         }

         filesAndDates.sort(DownloadCacheCleaner$PathAndTime.NEWEST_FIRST);
         List<DownloadCacheCleaner$PathAndPriority> filesWithDirOrder = prioritizeFilesInDirs(filesAndDates);
         Collections.reverse(filesWithDirOrder);
         filesWithDirOrder.sort(DownloadCacheCleaner$PathAndPriority.HIGHEST_PRIORITY_FIRST);
         Set<Path> emptyDirectoryCandidates = new HashSet<>();

         for (int i = 0; i < toRemove; i++) {
            DownloadCacheCleaner$PathAndPriority entry = filesWithDirOrder.get(i);
            Path pathToRemove = entry.path;

            try {
               Files.delete(pathToRemove);
               if (entry.removalPriority == 0) {
                  emptyDirectoryCandidates.add(pathToRemove.getParent());
               }
            } catch (IOException var12) {
               LOGGER.warn("Failed to delete cache file {}", pathToRemove, var12);
            }
         }

         emptyDirectoryCandidates.remove(cacheDir);

         for (Path dir : emptyDirectoryCandidates) {
            try {
               Files.delete(dir);
            } catch (DirectoryNotEmptyException var10) {
            } catch (IOException var11) {
               LOGGER.warn("Failed to delete empty(?) cache directory {}", dir, var11);
            }
         }
      } catch (UncheckedIOException | IOException var13) {
         LOGGER.error("Failed to vacuum cache dir {}", cacheDir, var13);
      }
   }

   private static List<DownloadCacheCleaner$PathAndTime> listFilesWithModificationTimes(final Path cacheDir) throws IOException {
      try {
         List<DownloadCacheCleaner$PathAndTime> unsortedFiles = new ArrayList<>();
         Files.walkFileTree(cacheDir, new DownloadCacheCleaner$1(cacheDir, unsortedFiles));
         return unsortedFiles;
      } catch (NoSuchFileException var2) {
         return List.of();
      }
   }

   private static List<DownloadCacheCleaner$PathAndPriority> prioritizeFilesInDirs(final List<DownloadCacheCleaner$PathAndTime> filesAndDates) {
      List<DownloadCacheCleaner$PathAndPriority> result = new ArrayList<>();
      Object2IntOpenHashMap<Path> parentCounts = new Object2IntOpenHashMap();

      for (DownloadCacheCleaner$PathAndTime entry : filesAndDates) {
         int removalPriority = parentCounts.addTo(entry.path.getParent(), 1);
         result.add(new DownloadCacheCleaner$PathAndPriority(entry.path, removalPriority));
      }

      return result;
   }
}
