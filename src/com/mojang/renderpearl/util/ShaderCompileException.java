package com.mojang.renderpearl.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ShaderCompileException extends Exception {
   public ShaderCompileException(final String message) {
      super(message);
   }
}
