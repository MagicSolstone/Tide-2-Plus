package net.minecraft.server.packs.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import java.util.Optional;
import net.minecraft.server.packs.metadata.MetadataSectionType;

class ResourceMetadata$2 implements ResourceMetadata {
   ResourceMetadata$2(final JsonObject val$metadata) {
      this.val$metadata = val$metadatax;
   }

   @Override
   public <T> Optional<T> getSection(final MetadataSectionType<T> serializer) {
      String name = serializer.name();
      JsonElement rawSection = this.val$metadata.get(name);
      if (rawSection != null) {
         T section = (T)serializer.codec().parse(JsonOps.INSTANCE, rawSection).getOrThrow(JsonParseException::new);
         return Optional.of(section);
      } else {
         return Optional.empty();
      }
   }
}
