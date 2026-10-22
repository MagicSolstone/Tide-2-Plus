package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.GpuFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record Uniform$Utb(int samplerIndex, GpuFormat format, int texture) implements Uniform {
   public Uniform$Utb(final int samplerIndex, final GpuFormat format) {
      this(samplerIndex, format, GlStateManager._genTexture());
   }

   @Override
   public void close() {
      GlStateManager._deleteTexture(this.texture);
   }
}
