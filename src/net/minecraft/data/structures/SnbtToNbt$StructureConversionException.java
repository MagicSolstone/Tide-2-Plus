package net.minecraft.data.structures;

import java.nio.file.Path;

class SnbtToNbt$StructureConversionException extends RuntimeException {
   public SnbtToNbt$StructureConversionException(final Path path, final Throwable t) {
      super(path.toAbsolutePath().toString(), t);
   }
}
