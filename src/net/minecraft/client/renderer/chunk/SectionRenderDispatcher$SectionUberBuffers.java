package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.vertex.UberGpuBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record SectionRenderDispatcher$SectionUberBuffers(UberGpuBuffer<SectionMesh> vertexBuffer, UberGpuBuffer<SectionMesh> indexBuffer) {
}
