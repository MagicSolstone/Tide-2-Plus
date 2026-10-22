package net.minecraft.client.renderer;

import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record ShaderManager$PendingResults(
   ShaderManager$Configs configs,
   List<RenderPipeline> requiredPipelines,
   List<RenderPipeline> optionalPipelines,
   List<CompiledRenderPipeline> compiledRequiredPipelines,
   List<CompiledRenderPipeline> compiledOptionalPipelines
) {
}
