package net.minecraft.server.packs;

import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.server.packs.repository.Pack$Metadata;
import net.minecraft.server.packs.repository.Pack$ResourcesSupplier;

class VanillaPackResources$1 implements Pack$ResourcesSupplier {
   VanillaPackResources$1(final VanillaPackResources this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public PackMetadataResources openMetadata(final PackLocationInfo location) {
      return this.this$0.fullResources;
   }

   @Override
   public Stream<PackResources> openResources(final PackLocationInfo location, final Pack$Metadata metadata) {
      return this.this$0.resourceLayers.stream();
   }
}
