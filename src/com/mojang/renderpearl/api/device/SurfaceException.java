package com.mojang.renderpearl.api.device;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SurfaceException extends Exception {
   public SurfaceException(final String message) {
      super(message);
   }

   public SurfaceException(final Throwable cause) {
      super(cause);
   }
}
