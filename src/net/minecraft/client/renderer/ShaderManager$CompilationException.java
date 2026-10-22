package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ShaderManager$CompilationException extends Exception {
   public ShaderManager$CompilationException(final String message) {
      super(message);
   }
}
