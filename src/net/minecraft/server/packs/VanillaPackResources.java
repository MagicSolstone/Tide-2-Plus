package net.minecraft.server.packs;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.Pack$ResourcesSupplier;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;

public class VanillaPackResources {
   private final FixedPathPackResources fullResources;
   private final List<PackResources> resourceLayers;

   public VanillaPackResources(final FixedPathPackResources fullResources, final List<PackResources> resourceLayers) {
      this.fullResources = fullResources;
      this.resourceLayers = resourceLayers;
   }

   public void listRawPaths(final PackType type, final Identifier resource, final Consumer<Path> output) {
      this.fullResources.listRawPaths(type, resource, output);
   }

   public PackResources fullResources() {
      return this.fullResources;
   }

   public Pack$ResourcesSupplier asResourcesSupplier() {
      return new VanillaPackResources$1(this);
   }

   public ResourceManager asResourceManager() {
      FallbackResourceManager vanillaOnlyResourceManager = new FallbackResourceManager(PackType.CLIENT_RESOURCES, "minecraft");
      vanillaOnlyResourceManager.push(this.fullResources);
      return vanillaOnlyResourceManager;
   }
}
