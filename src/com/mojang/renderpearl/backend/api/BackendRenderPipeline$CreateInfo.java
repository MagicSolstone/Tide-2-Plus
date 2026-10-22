package com.mojang.renderpearl.backend.api;

import com.mojang.renderpearl.api.pipeline.BindGroupLayout$UniformDescription;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PolygonMode;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record BackendRenderPipeline$CreateInfo(
   String name,
   List<BackendRenderPipeline$CreateInfo$Shader> shaders,
   List<BackendRenderPipeline$CreateInfo$VertexBuffer> vertexBuffers,
   List<BackendRenderPipeline$CreateInfo$AttribBinding> attribBindings,
   List<BindGroupLayout$UniformDescription> uniforms,
   int pushConstantsSize,
   @Nullable DepthStencilState depthStencilState,
   PolygonMode polygonMode,
   boolean cull,
   List<ColorTargetState> colorTargetStates,
   PrimitiveTopology primitiveTopology
) {
}
