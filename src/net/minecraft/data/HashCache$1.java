package net.minecraft.data;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.mutable.MutableInt;

class HashCache$1 extends SimpleFileVisitor<Path> {
   HashCache$1(final HashCache this$0, final MutableInt val$found, final Set val$allowedFiles, final MutableInt val$removed) {
      this.val$found = val$foundx;
      this.val$allowedFiles = val$allowedFilesx;
      this.val$removed = val$removedx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
      if (this.this$0.cachePaths.contains(file)) {
         return FileVisitResult.CONTINUE;
      } else {
         this.val$found.increment();
         if (this.val$allowedFiles.contains(file)) {
            return FileVisitResult.CONTINUE;
         } else {
            try {
               Files.delete(file);
            } catch (IOException var4) {
               HashCache.LOGGER.warn("Failed to delete file {}", file, var4);
            }

            this.val$removed.increment();
            return FileVisitResult.CONTINUE;
         }
      }
   }
}
