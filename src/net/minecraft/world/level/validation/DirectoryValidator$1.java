package net.minecraft.world.level.validation;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;

class DirectoryValidator$1 extends SimpleFileVisitor<Path> {
   DirectoryValidator$1(final DirectoryValidator this$0, final List val$issues) {
      this.val$issues = val$issuesx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   private void validateSymlink(final Path path, final BasicFileAttributes attrs) throws IOException {
      if (attrs.isSymbolicLink()) {
         this.this$0.validateSymlink(path, this.val$issues);
      }
   }

   public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
      this.validateSymlink(dir, attrs);
      return super.preVisitDirectory(dir, attrs);
   }

   public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
      this.validateSymlink(file, attrs);
      return super.visitFile(file, attrs);
   }
}
