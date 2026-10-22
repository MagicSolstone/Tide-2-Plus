package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem$AutoStorageIndexBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.oit.OitRenderPassProvider;
import net.minecraft.client.renderer.oit.OitRenderPassProvider$Parameters;
import net.minecraft.client.renderer.oit.OitStage;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class ChunkSectionsToRender {
   private final int maxIndicesRequired;
   private final GpuBufferSlice terrainTransformUBO;

   private ChunkSectionsToRender(final GpuBufferSlice terrainTransformUBO, final int maxIndicesRequired) {
      this.maxIndicesRequired = maxIndicesRequired;
      this.terrainTransformUBO = terrainTransformUBO;
   }

   protected abstract void render(
      final ChunkSectionLayer layer,
      final RenderPass renderPass,
      @Nullable GpuBuffer defaultIndexBuffer,
      @Nullable IndexType defaultIndexType,
      @Nullable final RenderPipeline renderPipelineOverride,
      @Nullable final RenderPipeline renderPipelineOverrideMultidraw
   );

   public void renderGroup(
      final ChunkSectionLayerGroup group,
      final RenderPass renderPass,
      final GpuSampler sampler,
      final GpuTextureView atlas,
      final boolean renderWireframeTerrain
   ) {
      GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
      GpuTextureView lightmap = gameRenderer.lightmap();
      this.renderLayers(
         group.layers(),
         sampler,
         renderPass,
         atlas,
         lightmap,
         renderWireframeTerrain ? RenderPipelines.WIREFRAME : null,
         renderWireframeTerrain ? RenderPipelines.WIREFRAME_MULTIDRAW : null
      );
   }

   public void renderOit(
      final GpuSampler sampler, final OitStage stage, final OitRenderPassProvider$Parameters params, final GpuTextureView atlas, final GpuTextureView lightmap
   ) {
      try (RenderPass renderPass = OitRenderPassProvider.createRenderPass(stage, () -> "Terrain", params)) {
         this.renderLayers(
            ChunkSectionLayerGroup.TRANSLUCENT.layers(),
            sampler,
            renderPass,
            atlas,
            lightmap,
            RenderPipelines.OIT_TERRAIN.getPipeline(stage),
            RenderPipelines.OIT_TERRAIN_MULTIDRAW.getPipeline(stage)
         );
      }
   }

   private void renderLayers(
      final ChunkSectionLayer[] layers,
      final GpuSampler sampler,
      final RenderPass renderPass,
      final GpuTextureView atlas,
      final GpuTextureView lightmap,
      @Nullable final RenderPipeline renderPipelineOverride,
      @Nullable final RenderPipeline renderPipelineOverrideMultidraw
   ) {
      RenderSystem$AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
      GpuBuffer defaultIndexBuffer = this.maxIndicesRequired == 0 ? null : autoIndices.getBuffer();
      IndexType defaultIndexType = this.maxIndicesRequired == 0 ? null : autoIndices.type();
      renderPass.setUniform("TerrainUniform", this.terrainTransformUBO);
      renderPass.setUniform("Sampler0", atlas, sampler);
      renderPass.setUniform("Sampler2", lightmap, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));

      for (ChunkSectionLayer layer : layers) {
         renderPass.pushDebugGroup(() -> "Terrain layer: " + layer.label());
         this.render(layer, renderPass, defaultIndexBuffer, defaultIndexType, renderPipelineOverride, renderPipelineOverrideMultidraw);
         renderPass.popDebugGroup();
      }
   }
}
