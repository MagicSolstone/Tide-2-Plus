package net.minecraft.client.gui.screens.packs;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class PackSelectionScreen$Watcher implements AutoCloseable {
   private final WatchService watcher;
   private final Path packPath;

   public PackSelectionScreen$Watcher(final Path packPath) throws IOException {
      this.packPath = packPath;
      this.watcher = packPath.getFileSystem().newWatchService();

      try {
         this.watchDir(packPath);

         try (DirectoryStream<Path> paths = Files.newDirectoryStream(packPath)) {
            for (Path path : paths) {
               if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                  this.watchDir(path);
               }
            }
         }
      } catch (Exception var7) {
         this.watcher.close();
         throw var7;
      }
   }

   @Nullable
   public static PackSelectionScreen$Watcher create(final Path packDir) {
      try {
         return new PackSelectionScreen$Watcher(packDir);
      } catch (IOException var2) {
         PackSelectionScreen.LOGGER.warn("Failed to initialize pack directory {} monitoring", packDir, var2);
         return null;
      }
   }

   private void watchDir(final Path packPath) throws IOException {
      packPath.register(this.watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
   }

   public boolean pollForChanges() throws IOException {
      boolean hasChanges = false;

      WatchKey key;
      while ((key = this.watcher.poll()) != null) {
         for (WatchEvent<?> watchEvent : key.pollEvents()) {
            hasChanges = true;
            if (key.watchable() == this.packPath && watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
               Path newPath = this.packPath.resolve((Path)watchEvent.context());
               if (Files.isDirectory(newPath, LinkOption.NOFOLLOW_LINKS)) {
                  this.watchDir(newPath);
               }
            }
         }

         key.reset();
      }

      return hasChanges;
   }

   @Override
   public void close() throws IOException {
      this.watcher.close();
   }
}
