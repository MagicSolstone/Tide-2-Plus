package net.minecraft.server.packs;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.server.packs.repository.Pack$Metadata;
import net.minecraft.server.packs.repository.Pack$ResourcesSupplier;

public class FilePackResources$FileResourcesSupplier implements Pack$ResourcesSupplier {
   private final File content;

   public FilePackResources$FileResourcesSupplier(final Path content) {
      this(content.toFile());
   }

   public FilePackResources$FileResourcesSupplier(final File content) {
      this.content = content;
   }

   @Override
   public PackMetadataResources openMetadata(final PackLocationInfo location) {
      FilePackResources$SharedZipFileAccess fileAccess = new FilePackResources$SharedZipFileAccess(this.content);
      return new FilePackResources(location, fileAccess, "");
   }

   @Override
   public Stream<PackResources> openResources(final PackLocationInfo location, final Pack$Metadata metadata) {
      FilePackResources$SharedZipFileAccess fileAccess = new FilePackResources$SharedZipFileAccess(this.content);
      PackResources primary = new FilePackResources(location, fileAccess, "");
      List<String> overlays = metadata.overlays();
      if (overlays.isEmpty()) {
         return Stream.of(primary);
      } else {
         List<PackResources> overlayResources = new ArrayList<>(overlays.size());

         for (String overlay : overlays) {
            overlayResources.add(new FilePackResources(location, fileAccess, overlay));
         }

         return Stream.of(new OverlayedPackResources(primary, overlayResources));
      }
   }
}
