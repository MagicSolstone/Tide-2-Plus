package net.minecraft.util.filefix.access;

import java.nio.file.Path;
import java.util.function.BiConsumer;

public enum CompressedNbt$MissingSeverity {
   IMPORTANT(CompressedNbt.LOGGER::error),
   NEUTRAL(CompressedNbt.LOGGER::info),
   MINOR(CompressedNbt.LOGGER::debug);

   private final BiConsumer<String, Object> logFunction;

   private CompressedNbt$MissingSeverity(final BiConsumer<String, Object> logFunction) {
      this.logFunction = logFunction;
   }

   public void log(final String message, final Path path) {
      this.logFunction.accept(message, path);
   }
}
