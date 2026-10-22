package net.minecraft.client.gui.screens.packs;

import java.nio.file.Path;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.repository.PackDetector;
import net.minecraft.world.level.validation.DirectoryValidator;

@Environment(EnvType.CLIENT)
class PackSelectionScreen$1 extends PackDetector<Path> {
   PackSelectionScreen$1(final PackSelectionScreen this$0, final DirectoryValidator validator) {
      Objects.requireNonNull(this$0);
      super(validator);
   }

   protected Path createZipPack(final Path content) {
      return content;
   }

   protected Path createDirectoryPack(final Path content) {
      return content;
   }
}
