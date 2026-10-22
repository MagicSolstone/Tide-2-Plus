package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TextureTarget extends RenderTarget {
   public TextureTarget(
      @Nullable final String label, final int width, final int height, @Nullable final GpuFormat colorFormat, @Nullable final GpuFormat depthFormat
   ) {
      super(label, colorFormat, depthFormat);
      RenderSystem.assertOnRenderThread();
      this.resize(width, height);
   }
}
