package net.minecraft.util.filefix;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.jspecify.annotations.Nullable;

class FileFixerUpper$1 extends SimpleFileVisitor<Path> {
   public FileVisitResult visitFile(final Path realPath, final BasicFileAttributes attrs) {
      try {
         Files.deleteIfExists(realPath);
      } catch (IOException var4) {
      }

      return FileVisitResult.CONTINUE;
   }

   public FileVisitResult postVisitDirectory(final Path realPath, @Nullable final IOException e) {
      try {
         Files.deleteIfExists(realPath);
      } catch (IOException var4) {
      }

      return FileVisitResult.CONTINUE;
   }
}
