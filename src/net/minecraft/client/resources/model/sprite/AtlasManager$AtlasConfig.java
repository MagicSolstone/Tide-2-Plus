package net.minecraft.client.resources.model.sprite;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.metadata.MetadataSectionType;

@Environment(EnvType.CLIENT)
public record AtlasManager$AtlasConfig(
   Identifier textureId, Identifier definitionLocation, boolean createMipmaps, Set<MetadataSectionType<?>> additionalMetadata
) {
   public AtlasManager$AtlasConfig(final Identifier textureId, final Identifier definitionLocation, final boolean createMipmaps) {
      this(textureId, definitionLocation, createMipmaps, Set.of());
   }
}
