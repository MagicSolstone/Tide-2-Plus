package net.minecraft.server.packs.resources;

import java.io.InputStream;
import net.minecraft.server.packs.PackResources;

record FallbackResourceManager$1ResourceWithSourceAndIndex(PackResources packResources, IoSupplier<InputStream> resource, int packIndex) {
}
