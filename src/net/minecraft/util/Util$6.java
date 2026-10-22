package net.minecraft.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;

class Util$6 implements BooleanSupplier {
   Util$6(final Path val$target) {
      this.val$target = val$targetx;
   }

   @Override
   public boolean getAsBoolean() {
      try {
         Files.deleteIfExists(this.val$target);
         return true;
      } catch (IOException var2) {
         Util.LOGGER.warn("Failed to delete", var2);
         return false;
      }
   }

   @Override
   public String toString() {
      return "delete old " + this.val$target;
   }
}
