package com.mojang.renderpearl.api.pipeline;

import com.mojang.renderpearl.api.GpuFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record BindGroupLayout$UniformDescription(String name, UniformType type, @Nullable GpuFormat gpuFormat) {
   public BindGroupLayout$UniformDescription(final String name, final UniformType type) {
      this(name, type, null);
      if (type == UniformType.TEXEL_BUFFER) {
         throw new IllegalArgumentException("Texel buffer needs a texture format");
      }
   }

   public BindGroupLayout$UniformDescription(final String name, final GpuFormat gpuFormat) {
      this(name, UniformType.TEXEL_BUFFER, gpuFormat);
   }
}
