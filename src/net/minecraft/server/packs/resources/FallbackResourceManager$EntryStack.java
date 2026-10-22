package net.minecraft.server.packs.resources;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;

record FallbackResourceManager$EntryStack(
   Identifier fileLocation,
   Identifier metadataLocation,
   List<FallbackResourceManager$ResourceWithSource> fileSources,
   Map<PackResources, IoSupplier<InputStream>> metaSources
) {
   public FallbackResourceManager$EntryStack(final Identifier fileLocation) {
      this(fileLocation, FallbackResourceManager.getMetadataLocation(fileLocation), new ArrayList<>(), new Object2ObjectArrayMap());
   }
}
