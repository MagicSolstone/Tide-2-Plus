package com.mojang.renderpearl.api.pipeline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record BindGroupLayout(List<BindGroupLayout$UniformDescription> uniforms) {
   public BindGroupLayout(List<BindGroupLayout$UniformDescription> uniforms) {
      uniforms = List.copyOf(uniforms);
      this.uniforms = uniforms;
   }

   public static BindGroupLayout$Builder builder() {
      return new BindGroupLayout$Builder();
   }

   public static List<BindGroupLayout$UniformDescription> flattenUniforms(final List<BindGroupLayout> bindGroupLayouts) {
      List<BindGroupLayout$UniformDescription> flattened = new ArrayList<>();

      for (BindGroupLayout bindGroupLayout : bindGroupLayouts) {
         flattened.addAll(bindGroupLayout.uniforms());
      }

      return flattened;
   }

   public static void ensureCompatible(final List<BindGroupLayout> bindGroupLayouts) {
      Set<String> names = new HashSet<>();

      for (int layoutIndex = 0; layoutIndex < bindGroupLayouts.size(); layoutIndex++) {
         BindGroupLayout bindGroupLayout = bindGroupLayouts.get(layoutIndex);

         for (BindGroupLayout$UniformDescription uniform : bindGroupLayout.uniforms()) {
            if (!names.add(uniform.name())) {
               throw new IllegalArgumentException("Duplicate bind name '" + uniform.name() + "' in bind group layout " + layoutIndex);
            }
         }
      }
   }
}
