package net.minecraft.util.filefix.virtualfilesystem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSCreationException;
import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSSymlinkException;

class CopyOnWriteFileSystem$1 extends SimpleFileVisitor<Path> {
   CopyOnWriteFileSystem$1(final CopyOnWriteFileSystem this$0, final DirectoryNode val$fileTree, final Path val$baseDirectory) {
      this.val$fileTree = val$fileTreex;
      this.val$baseDirectory = val$baseDirectoryx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public FileVisitResult visitFile(final Path realPath, final BasicFileAttributes attrs) throws IOException {
      checkAttributes(realPath, attrs);
      if (this.this$0.skippedPaths.matches(realPath)) {
         return FileVisitResult.CONTINUE;
      } else {
         CopyOnWriteFSPath cowPath = this.toCowPath(realPath);
         DirectoryNode parentNode = this.val$fileTree.directoryByPath(Objects.requireNonNull(cowPath.getParent()));
         parentNode.addChild(new FileNode(cowPath, realPath, false));
         return FileVisitResult.CONTINUE;
      }
   }

   public FileVisitResult preVisitDirectory(final Path realPath, final BasicFileAttributes attrs) throws IOException {
      checkAttributes(realPath, attrs);
      if (this.this$0.skippedPaths.matches(realPath)) {
         return FileVisitResult.SKIP_SUBTREE;
      } else if (realPath.equals(this.val$baseDirectory)) {
         return FileVisitResult.CONTINUE;
      } else {
         CopyOnWriteFSPath cowPath = this.toCowPath(realPath);
         DirectoryNode parentNode = this.val$fileTree.directoryByPath(Objects.requireNonNull(cowPath.getParent()));
         parentNode.addChild(new DirectoryNode(cowPath));
         return FileVisitResult.CONTINUE;
      }
   }

   private static void checkAttributes(final Path realPath, final BasicFileAttributes attrs) throws CowFSCreationException {
      if (!attrs.isRegularFile() && !attrs.isDirectory()) {
         throw new CowFSSymlinkException("Cannot build copy-on-write file system when symlink is present: " + realPath);
      } else if (!Files.isWritable(realPath)) {
         throw new CowFSCreationException("Cannot build copy-on-write file system, missing write access for file: " + realPath);
      }
   }

   private CopyOnWriteFSPath toCowPath(final Path realPath) {
      return this.val$fileTree.path().resolve(this.val$baseDirectory.relativize(realPath).toString());
   }
}
