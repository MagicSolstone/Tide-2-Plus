package com.mojang.renderpearl.api.commands;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.pipeline.IndexType;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RenderPass$Draw<T>(
   int slot,
   GpuBuffer vertexBuffer,
   @Nullable GpuBuffer indexBuffer,
   @Nullable IndexType indexType,
   int firstIndex,
   int indexCount,
   int baseVertex,
   @Nullable BiConsumer<T, RenderPass$UniformUploader> uniformUploaderConsumer
) {
   public RenderPass$Draw(
      final int slot,
      final GpuBuffer vertexBuffer,
      final GpuBuffer indexBuffer,
      final IndexType indexType,
      final int firstIndex,
      final int indexCount,
      final int baseVertex
   ) {
      this(slot, vertexBuffer, indexBuffer, indexType, firstIndex, indexCount, baseVertex, null);
   }
}
