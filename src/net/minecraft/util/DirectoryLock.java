package net.minecraft.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DirectoryLock implements AutoCloseable {
   public static final String LOCK_FILE = "session.lock";
   private final FileChannel lockFile;
   private final FileLock lock;
   private static final ByteBuffer DUMMY;

   public static DirectoryLock create(final Path dir) throws IOException {
      Path lockPath = dir.resolve("session.lock");
      FileUtil.createDirectoriesSafe(dir);
      FileChannel lockFile = FileChannel.open(lockPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

      try {
         lockFile.write(DUMMY.duplicate());
         lockFile.force(true);
         FileLock lock = lockFile.tryLock();
         if (lock == null) {
            throw DirectoryLock$LockException.alreadyLocked(lockPath);
         } else {
            return new DirectoryLock(lockFile, lock);
         }
      } catch (IOException var6) {
         try {
            lockFile.close();
         } catch (IOException var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }
   }

   private DirectoryLock(final FileChannel lockFile, final FileLock lock) {
      this.lockFile = lockFile;
      this.lock = lock;
   }

   @Override
   public void close() throws IOException {
      try {
         if (this.lock.isValid()) {
            this.lock.release();
         }
      } finally {
         if (this.lockFile.isOpen()) {
            this.lockFile.close();
         }
      }
   }

   public boolean isValid() {
      return this.lock.isValid();
   }

   public static boolean isLocked(final Path dir) throws IOException {
      Path lockPath = dir.resolve("session.lock");

      try {
         boolean var4;
         try (
            FileChannel lockFile = FileChannel.open(lockPath, StandardOpenOption.WRITE);
            FileLock maybeLock = lockFile.tryLock();
         ) {
            var4 = maybeLock == null;
         }

         return var4;
      } catch (AccessDeniedException var10) {
         return true;
      } catch (NoSuchFileException var11) {
         return false;
      }
   }

   static {
      byte[] chars = "☃".getBytes(StandardCharsets.UTF_8);
      DUMMY = ByteBuffer.allocateDirect(chars.length);
      DUMMY.put(chars);
      DUMMY.flip();
   }
}
