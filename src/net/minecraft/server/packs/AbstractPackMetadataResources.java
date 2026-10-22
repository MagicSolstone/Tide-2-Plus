package net.minecraft.server.packs;

import java.io.IOException;
import java.io.InputStream;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.jspecify.annotations.Nullable;

public abstract class AbstractPackMetadataResources implements PackMetadataResources {
   private final PackLocationInfo location;
   @Nullable
   private ResourceMetadata metadata;

   protected AbstractPackMetadataResources(final PackLocationInfo location) {
      this.location = location;
   }

   @Nullable
   @Override
   public <T> T getMetadataSection(final MetadataSectionType<T> metadataSerializer) throws IOException {
      if (this.metadata == null) {
         this.metadata = loadMetadata(this);
      }

      return this.metadata.getSection(metadataSerializer).orElse(null);
   }

   public static ResourceMetadata loadMetadata(final PackMetadataResources packResources) throws IOException {
      IoSupplier<InputStream> metadata = packResources.getRootResource("pack.mcmeta");
      if (metadata == null) {
         return ResourceMetadata.EMPTY;
      } else {
         ResourceMetadata var3;
         try (InputStream resource = metadata.get()) {
            var3 = ResourceMetadata.fromJsonStream(resource);
         }

         return var3;
      }
   }

   @Override
   public PackLocationInfo location() {
      return this.location;
   }
}
