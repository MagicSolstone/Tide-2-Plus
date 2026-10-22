package net.minecraft.data;

import java.nio.file.Path;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class PackOutput$PathProvider {
   private final Path root;
   private final String kind;

   private PackOutput$PathProvider(final PackOutput output, final PackOutput$Target target, final String kind) {
      this.root = output.getOutputFolder(target);
      this.kind = kind;
   }

   public Path file(final Identifier element, final String extension) {
      return element.withPath(path -> this.kind + "/" + path + "." + extension).resolveAgainst(this.root);
   }

   public Path json(final Identifier element) {
      return element.withPath(path -> this.kind + "/" + path + ".json").resolveAgainst(this.root);
   }

   public Path json(final ResourceKey<?> element) {
      return this.json(element.identifier());
   }
}
