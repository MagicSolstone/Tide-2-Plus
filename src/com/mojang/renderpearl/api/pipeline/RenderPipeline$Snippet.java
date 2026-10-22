package com.mojang.renderpearl.api.pipeline;

import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record RenderPipeline$Snippet(
   Map<ShaderType, Identifier> shaders,
   Optional<ShaderDefines> shaderDefines,
   Optional<List<BindGroupLayout>> bindGroupLayouts,
   ColorTargetState[] colorTargetStates,
   int activeColorTargetStateCount,
   Optional<DepthStencilState> depthStencilState,
   Optional<PolygonMode> polygonMode,
   Optional<Boolean> cull,
   VertexFormat[] vertexFormatPerBuffer,
   Optional<PrimitiveTopology> vertexFormatMode,
   int pushConstantSize
) {
}
