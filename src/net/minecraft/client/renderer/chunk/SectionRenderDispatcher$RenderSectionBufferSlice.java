package net.minecraft.client.renderer.chunk;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record SectionRenderDispatcher$RenderSectionBufferSlice(
   GpuBuffer vertexBuffer, long vertexBufferOffset, @Nullable GpuBuffer indexBuffer, long indexBufferOffset
) {
}
