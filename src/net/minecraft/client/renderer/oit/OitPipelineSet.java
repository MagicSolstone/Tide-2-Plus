package net.minecraft.client.renderer.oit;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline$Builder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record OitPipelineSet(RenderPipeline depthBoundsPipeline, RenderPipeline transmittancePipeline, RenderPipeline accumulatePipeline) {
   public RenderPipeline getPipeline(final OitStage stage) {
      return switch (stage) {
         case DEPTH_BOUNDS -> this.depthBoundsPipeline;
         case TRANSMITTANCE -> this.transmittancePipeline;
         case ACCUMULATE -> this.accumulatePipeline;
         default -> throw new IllegalArgumentException("Unsupported OIT stage.");
      };
   }

   public static OitPipelineSet$Builder builder(final String locationSuffix, final RenderPipeline$Builder builder) {
      return new OitPipelineSet$Builder(builder.buildSnippet(), locationSuffix);
   }
}
