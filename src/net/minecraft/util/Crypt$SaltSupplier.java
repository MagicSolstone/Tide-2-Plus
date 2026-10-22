package net.minecraft.util;

import java.security.SecureRandom;

public class Crypt$SaltSupplier {
   private static final SecureRandom secureRandom = new SecureRandom();

   public static long getLong() {
      return secureRandom.nextLong();
   }
}
