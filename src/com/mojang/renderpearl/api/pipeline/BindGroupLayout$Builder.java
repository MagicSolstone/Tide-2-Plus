package com.mojang.renderpearl.api.pipeline;

import com.mojang.renderpearl.api.GpuFormat;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BindGroupLayout$Builder {
   private final List<BindGroupLayout$UniformDescription> uniforms = new ArrayList<>();

   private BindGroupLayout$Builder() {
   }

   public BindGroupLayout$Builder withUniform(final String name, final UniformType type) {
      if (type == UniformType.TEXEL_BUFFER) {
         throw new IllegalArgumentException("Cannot use texel buffer without specifying texture format");
      } else {
         this.uniforms.add(new BindGroupLayout$UniformDescription(name, type));
         return this;
      }
   }

   public BindGroupLayout$Builder withUniform(final String name, final UniformType type, final GpuFormat format) {
      if (type != UniformType.TEXEL_BUFFER) {
         throw new IllegalArgumentException("Only texel buffer can specify texture format");
      } else {
         this.uniforms.add(new BindGroupLayout$UniformDescription(name, format));
         return this;
      }
   }

   public BindGroupLayout build() {
      return new BindGroupLayout(this.uniforms);
   }
}
