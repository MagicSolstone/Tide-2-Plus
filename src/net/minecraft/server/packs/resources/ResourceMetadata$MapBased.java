package net.minecraft.server.packs.resources;

import java.util.Map;
import java.util.Optional;
import net.minecraft.server.packs.metadata.MetadataSectionType;

public class ResourceMetadata$MapBased implements ResourceMetadata {
   private final Map<MetadataSectionType<?>, ?> values;

   private ResourceMetadata$MapBased(final Map<MetadataSectionType<?>, ?> values) {
      this.values = values;
   }

   @Override
   public <T> Optional<T> getSection(final MetadataSectionType<T> serializer) {
      return Optional.ofNullable((T)this.values.get(serializer));
   }
}
