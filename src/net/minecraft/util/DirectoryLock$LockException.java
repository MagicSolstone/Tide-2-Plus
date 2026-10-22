package net.minecraft.util;

import java.io.IOException;
import java.nio.file.Path;

public class DirectoryLock$LockException extends IOException {
   private DirectoryLock$LockException(final Path path, final String message) {
      super(path.toAbsolutePath() + ": " + message);
   }

   public static DirectoryLock$LockException alreadyLocked(final Path path) {
      return new DirectoryLock$LockException(path, "already locked (possibly by other Minecraft instance?)");
   }
}
