package com.mojang.renderpearl.api.device;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GpuOutOfMemoryException extends RuntimeException {
   public GpuOutOfMemoryException(final String message) {
      super(message);
   }
}
