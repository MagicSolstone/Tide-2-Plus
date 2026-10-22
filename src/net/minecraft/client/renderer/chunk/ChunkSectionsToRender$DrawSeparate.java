package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.commands.RenderPass$Draw;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ChunkSectionsToRender$DrawSeparate extends ChunkSectionsToRender {
   private final Map<ChunkSectionLayer, List<RenderPass$Draw<GpuBufferSlice[]>>> drawsPerLayer;
   private final GpuBufferSlice[] chunkSectionInfos;

   public ChunkSectionsToRender$DrawSeparate(
      final GpuBufferSlice terrainTransformUBO,
      final Map<ChunkSectionLayer, List<RenderPass$Draw<GpuBufferSlice[]>>> drawsPerLayer,
      final int maxIndicesRequired,
      final GpuBufferSlice[] chunkSectionInfos
   ) {
      super(terrainTransformUBO, maxIndicesRequired);
      this.drawsPerLayer = drawsPerLayer;
      this.chunkSectionInfos = chunkSectionInfos;
   }

   @Override
   protected void render(
      final ChunkSectionLayer layer,
      final RenderPass renderPass,
      @Nullable final GpuBuffer defaultIndexBuffer,
      @Nullable final IndexType defaultIndexType,
      @Nullable final RenderPipeline renderPipelineOverride,
      @Nullable final RenderPipeline renderPipelineOverrideMultidraw
   ) {
      renderPass.setPipeline(RenderSystem.getCompiledPipeline(renderPipelineOverride != null ? renderPipelineOverride : layer.pipeline(false)));
      List<RenderPass$Draw<GpuBufferSlice[]>> draws = this.drawsPerLayer.get(layer);
      if (!draws.isEmpty()) {
         renderPass.drawMultipleIndexed(draws, defaultIndexBuffer, defaultIndexType, List.of("ChunkSection"), this.chunkSectionInfos);
      }
   }
}
