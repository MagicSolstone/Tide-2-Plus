package net.minecraft.data;

import java.nio.file.Path;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

public class PackOutput {
   private final Path outputFolder;

   public PackOutput(final Path outputFolder) {
      this.outputFolder = outputFolder;
   }

   public Path getOutputFolder() {
      return this.outputFolder;
   }

   public Path getOutputFolder(final PackOutput$Target target) {
      return this.getOutputFolder().resolve(target.directory);
   }

   public PackOutput$PathProvider createPathProvider(final PackOutput$Target target, final String kind) {
      return new PackOutput$PathProvider(this, target, kind);
   }

   public PackOutput$PathProvider createRegistryElementsPathProvider(final ResourceKey<? extends Registry<?>> registryKey) {
      return this.createPathProvider(PackOutput$Target.DATA_PACK, Registries.elementsDirPath(registryKey));
   }

   public PackOutput$PathProvider createRegistryTagsPathProvider(final ResourceKey<? extends Registry<?>> registryKey) {
      return this.createPathProvider(PackOutput$Target.DATA_PACK, Registries.tagsDirPath(registryKey));
   }

   public PackOutput$PathProvider createRegistryComponentPathProvider(final ResourceKey<? extends Registry<?>> registryKey) {
      return this.createPathProvider(PackOutput$Target.REPORTS, Registries.componentsDirPath(registryKey));
   }
}
