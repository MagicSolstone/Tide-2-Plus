package net.minecraft.server.packs.repository;

import java.util.stream.Stream;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackMetadataResources;
import net.minecraft.server.packs.PackResources;

public interface Pack$ResourcesSupplier {
   PackMetadataResources openMetadata(PackLocationInfo location);

   Stream<PackResources> openResources(PackLocationInfo location, Pack$Metadata metadata);
}
