package net.minecraft.server.packs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.server.packs.repository.Pack$Metadata;
import net.minecraft.server.packs.repository.Pack$ResourcesSupplier;

public class PathPackResources$PathResourcesSupplier implements Pack$ResourcesSupplier {
   private final Path content;

   public PathPackResources$PathResourcesSupplier(final Path content) {
      this.content = content;
   }

   private PathPackResources openPrimaryResources(final PackLocationInfo location) {
      return new PathPackResources(location, this.content);
   }

   @Override
   public PackMetadataResources openMetadata(final PackLocationInfo location) {
      return this.openPrimaryResources(location);
   }

   @Override
   public Stream<PackResources> openResources(final PackLocationInfo location, final Pack$Metadata metadata) {
      PackResources primary = this.openPrimaryResources(location);
      List<String> overlays = metadata.overlays();
      if (overlays.isEmpty()) {
         return Stream.of(primary);
      } else {
         List<PackResources> overlayResources = new ArrayList<>(overlays.size());

         for (String overlay : overlays) {
            Path overlayRoot = this.content.resolve(overlay);
            overlayResources.add(new PathPackResources(location, overlayRoot));
         }

         return Stream.of(new OverlayedPackResources(primary, overlayResources));
      }
   }
}
