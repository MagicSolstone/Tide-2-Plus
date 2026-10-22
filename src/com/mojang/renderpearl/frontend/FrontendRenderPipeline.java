package com.mojang.renderpearl.frontend;

import com.mojang.renderpearl.api.pipeline.BindGroupLayout$UniformDescription;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import com.mojang.renderpearl.backend.api.BackendRenderPipeline;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record FrontendRenderPipeline(
   String name,
   BackendRenderPipeline backendRenderPipeline,
   List<VertexFormat> vertexFormats,
   Object2IntMap<String> uniformIndices,
   List<BindGroupLayout$UniformDescription> uniforms,
   List<ColorTargetState> colorTargetStates,
   boolean wantsDepthTexture,
   int pushConstantSize
) implements CompiledRenderPipeline {
   @Override
   public boolean isClosed() {
      return this.backendRenderPipeline().isClosed();
   }

   @Override
   public void close() {
      this.backendRenderPipeline.close();
   }
}
