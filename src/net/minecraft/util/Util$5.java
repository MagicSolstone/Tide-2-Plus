package net.minecraft.util;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;

class Util$5 implements BooleanSupplier {
   Util$5(final Path val$from, final Path val$to, final CopyOption[] val$options) {
      this.val$from = val$fromx;
      this.val$to = val$tox;
      this.val$options = val$optionsx;
   }

   @Override
   public boolean getAsBoolean() {
      try {
         Files.move(this.val$from, this.val$to, this.val$options);
         return true;
      } catch (IOException var2) {
         Util.LOGGER.error("Failed to rename", var2);
         return false;
      }
   }

   @Override
   public String toString() {
      return "rename " + this.val$from + " to " + this.val$to;
   }
}
