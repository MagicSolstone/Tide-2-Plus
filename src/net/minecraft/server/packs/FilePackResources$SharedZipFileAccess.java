package net.minecraft.server.packs;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.Nullable;

class FilePackResources$SharedZipFileAccess implements AutoCloseable {
   private final File file;
   @Nullable
   private ZipFile zipFile;
   private boolean failedToLoad;

   private FilePackResources$SharedZipFileAccess(final File file) {
      this.file = file;
   }

   @Nullable
   private ZipFile getOrCreateZipFile() {
      if (this.failedToLoad) {
         return null;
      } else {
         if (this.zipFile == null) {
            try {
               this.zipFile = new ZipFile(this.file);
            } catch (IOException var2) {
               FilePackResources.LOGGER.error("Failed to open pack {}", this.file, var2);
               this.failedToLoad = true;
               return null;
            }
         }

         return this.zipFile;
      }
   }

   @Override
   public void close() {
      if (this.zipFile != null) {
         IOUtils.closeQuietly(this.zipFile);
         this.zipFile = null;
      }
   }

   @Override
   protected void finalize() throws Throwable {
      this.close();
      super.finalize();
   }
}
