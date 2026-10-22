package net.minecraft.server.packs.resources;

import java.util.Optional;
import net.minecraft.server.packs.metadata.MetadataSectionType;

class ResourceMetadata$1 implements ResourceMetadata {
   @Override
   public <T> Optional<T> getSection(final MetadataSectionType<T> serializer) {
      return Optional.empty();
   }
}
