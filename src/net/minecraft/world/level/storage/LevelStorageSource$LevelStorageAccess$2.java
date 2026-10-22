package net.minecraft.world.level.storage;

import com.google.common.io.Files;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class LevelStorageSource$LevelStorageAccess$2 extends SimpleFileVisitor<Path> {
   LevelStorageSource$LevelStorageAccess$2(final LevelStorageSource$LevelStorageAccess this$1, final Path val$rootPath, final ZipOutputStream val$stream) {
      this.val$rootPath = val$rootPathx;
      this.val$stream = val$streamx;
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
   }

   public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
      if (path.endsWith("session.lock")) {
         return FileVisitResult.CONTINUE;
      } else {
         String entryPath = this.val$rootPath.resolve(this.this$1.levelDirectory.path().relativize(path)).toString().replace('\\', '/');
         ZipEntry entry = new ZipEntry(entryPath);
         this.val$stream.putNextEntry(entry);
         Files.asByteSource(path.toFile()).copyTo(this.val$stream);
         this.val$stream.closeEntry();
         return FileVisitResult.CONTINUE;
      }
   }
}
