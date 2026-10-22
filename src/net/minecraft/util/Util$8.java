package net.minecraft.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;

class Util$8 implements BooleanSupplier {
   Util$8(final Path val$target) {
      this.val$target = val$targetx;
   }

   @Override
   public boolean getAsBoolean() {
      return Files.isRegularFile(this.val$target);
   }

   @Override
   public String toString() {
      return "verify that " + this.val$target + " is present";
   }
}
