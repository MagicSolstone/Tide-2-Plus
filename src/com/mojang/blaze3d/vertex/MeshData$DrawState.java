package com.mojang.blaze3d.vertex;

import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record MeshData$DrawState(VertexFormat format, int vertexCount, int indexCount, PrimitiveTopology primitiveTopology, IndexType indexType) {
}
