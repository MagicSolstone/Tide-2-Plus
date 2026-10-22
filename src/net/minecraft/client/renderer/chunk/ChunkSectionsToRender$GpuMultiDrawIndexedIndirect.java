package net.minecraft.client.renderer.chunk;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.pipeline.IndexType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ChunkSectionsToRender$GpuMultiDrawIndexedIndirect(
   GpuBufferSlice vertexBuffer, @Nullable GpuBufferSlice indexBuffer, @Nullable IndexType indexType, GpuBufferSlice indirectCommandBuffer, int drawCount
) {
}
