package net.minecraft.server.packs;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

class DownloadCacheCleaner$1 extends SimpleFileVisitor<Path> {
   DownloadCacheCleaner$1(final Path val$cacheDir, final List val$unsortedFiles) {
      this.val$cacheDir = val$cacheDirx;
      this.val$unsortedFiles = val$unsortedFilesx;
   }

   public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
      if (attrs.isRegularFile() && !file.getParent().equals(this.val$cacheDir)) {
         FileTime fileTime = attrs.lastModifiedTime();
         this.val$unsortedFiles.add(new DownloadCacheCleaner$PathAndTime(file, fileTime));
      }

      return FileVisitResult.CONTINUE;
   }
}
