package net.minecraft.world.level.storage;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

class LevelStorageSource$LevelStorageAccess$1 extends SimpleFileVisitor<Path> {
   LevelStorageSource$LevelStorageAccess$1(final LevelStorageSource$LevelStorageAccess this$1, final Path val$lockPath) {
      this.val$lockPath = val$lockPathx;
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
   }

   public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
      if (!file.equals(this.val$lockPath)) {
         LevelStorageSource.LOGGER.debug("Deleting {}", file);
         Files.deleteIfExists(file);
      }

      return FileVisitResult.CONTINUE;
   }

   public FileVisitResult postVisitDirectory(final Path dir, @Nullable final IOException exc) throws IOException {
      if (exc != null) {
         throw exc;
      } else {
         if (dir.equals(this.this$1.levelDirectory.path())) {
            this.this$1.lock.close();
            Files.deleteIfExists(this.val$lockPath);
         }

         Files.deleteIfExists(dir);
         return FileVisitResult.CONTINUE;
      }
   }
}
