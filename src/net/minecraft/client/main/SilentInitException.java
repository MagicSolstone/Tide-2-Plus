package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SilentInitException extends RuntimeException {
   public SilentInitException(final String message) {
      super(message);
   }

   public SilentInitException(final String message, final Throwable cause) {
      super(message, cause);
   }
}
