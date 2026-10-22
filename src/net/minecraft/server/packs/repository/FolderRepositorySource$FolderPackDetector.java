package net.minecraft.server.packs.repository;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import net.minecraft.server.packs.FilePackResources$FileResourcesSupplier;
import net.minecraft.server.packs.PathPackResources$PathResourcesSupplier;
import net.minecraft.server.packs.linkfs.LinkFileSystem;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.jspecify.annotations.Nullable;

class FolderRepositorySource$FolderPackDetector extends PackDetector<Pack$ResourcesSupplier> {
   protected FolderRepositorySource$FolderPackDetector(final DirectoryValidator validator) {
      super(validator);
   }

   @Nullable
   protected Pack$ResourcesSupplier createZipPack(final Path content) {
      FileSystem fileSystem = content.getFileSystem();
      if (fileSystem != FileSystems.getDefault() && !(fileSystem instanceof LinkFileSystem)) {
         FolderRepositorySource.LOGGER.info("Can't open pack archive at {}", content);
         return null;
      } else {
         return new FilePackResources$FileResourcesSupplier(content);
      }
   }

   protected Pack$ResourcesSupplier createDirectoryPack(final Path content) {
      return new PathPackResources$PathResourcesSupplier(content);
   }
}
