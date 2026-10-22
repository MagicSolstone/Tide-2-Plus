package net.minecraft.server.packs.resources;

import java.io.InputStream;
import net.minecraft.server.packs.PackResources;

record FallbackResourceManager$ResourceWithSource(PackResources source, IoSupplier<InputStream> resource) {
}
