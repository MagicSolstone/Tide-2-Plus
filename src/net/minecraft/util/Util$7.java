package net.minecraft.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;

class Util$7 implements BooleanSupplier {
   Util$7(final Path val$target) {
      this.val$target = val$targetx;
   }

   @Override
   public boolean getAsBoolean() {
      return !Files.exists(this.val$target);
   }

   @Override
   public String toString() {
      return "verify that " + this.val$target + " is deleted";
   }
}
