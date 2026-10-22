package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.DeviceLimits;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.util.EnumMap;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ChunkSectionsToRender$DrawIndirect extends ChunkSectionsToRender {
   private final EnumMap<ChunkSectionLayer, List<ChunkSectionsToRender$GpuMultiDrawIndexedIndirect>> drawGroupsPerLayer;
   private final GpuBufferSlice chunkSectionInfos;

   public ChunkSectionsToRender$DrawIndirect(
      final GpuBufferSlice terrainTransformUBO,
      final EnumMap<ChunkSectionLayer, List<ChunkSectionsToRender$GpuMultiDrawIndexedIndirect>> drawGroupsPerLayer,
      final int maxIndicesRequired,
      final GpuBufferSlice chunkSectionInfos
   ) {
      super(terrainTransformUBO, maxIndicesRequired);
      this.drawGroupsPerLayer = drawGroupsPerLayer;
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
      renderPass.setPipeline(RenderSystem.getCompiledPipeline(renderPipelineOverrideMultidraw != null ? renderPipelineOverrideMultidraw : layer.pipeline(true)));
      DeviceLimits deviceLimits = RenderSystem.getDevice().getDeviceInfo().limits();
      int maxDrawIndirectDrawCount = deviceLimits.maxDrawIndirectDrawCount();
      List<ChunkSectionsToRender$GpuMultiDrawIndexedIndirect> drawGroups = this.drawGroupsPerLayer.get(layer);
      if (!drawGroups.isEmpty()) {
         renderPass.setVertexBuffer(1, this.chunkSectionInfos);
      }

      for (ChunkSectionsToRender$GpuMultiDrawIndexedIndirect indirectDraw : drawGroups) {
         if (indirectDraw.drawCount() > 0) {
            renderPass.setVertexBuffer(0, indirectDraw.vertexBuffer);
            IndexType indexType = indirectDraw.indexType() == null ? defaultIndexType : indirectDraw.indexType();
            renderPass.setIndexBuffer(indirectDraw.indexBuffer() == null ? defaultIndexBuffer : indirectDraw.indexBuffer().buffer(), indexType);
            GpuBuffer buffer = indirectDraw.indirectCommandBuffer.buffer();
            long startOffset = indirectDraw.indirectCommandBuffer().offset();
            int remainingDrawCount = indirectDraw.drawCount();

            while (remainingDrawCount > 0) {
               int passDrawCount = Integer.min(remainingDrawCount, maxDrawIndirectDrawCount);
               long length = passDrawCount * 20L;
               GpuBufferSlice passSlice = buffer.slice(startOffset, length);
               renderPass.drawIndexedIndirect(passSlice, passDrawCount);
               remainingDrawCount -= passDrawCount;
               startOffset += length;
            }
         }
      }
   }
}
