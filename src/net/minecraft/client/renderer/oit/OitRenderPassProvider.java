package net.minecraft.client.renderer.oit;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.commands.RenderPassDescriptor;
import com.mojang.renderpearl.api.commands.RenderPassDescriptor$Builder;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LevelRenderer;

@Environment(EnvType.CLIENT)
public class OitRenderPassProvider {
   public static RenderPass createRenderPass(final OitStage stage, final Supplier<String> label, final OitRenderPassProvider$Parameters params) {
      return switch (stage) {
         case DEPTH_BOUNDS -> createDepthBoundsPass(label, params);
         case TRANSMITTANCE -> createTransmittancePass(label, params);
         case ACCUMULATE -> createAccumulatePass(label, params);
         default -> throw new IllegalArgumentException("Invalid OIT stage.");
      };
   }

   private static RenderPass createDepthBoundsPass(final Supplier<String> label, final OitRenderPassProvider$Parameters params) {
      RenderPassDescriptor descriptor = RenderPassDescriptor.builder(() -> "OIT Depth Bounds for " + label.get())
         .withColorAttachment(params.depthBoundsTargetView)
         .withDepthAttachment(params.depthTextureView)
         .build();
      RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(descriptor);
      RenderSystem.bindDefaultUniforms(renderPass);
      return renderPass;
   }

   private static RenderPass createTransmittancePass(final Supplier<String> label, final OitRenderPassProvider$Parameters params) {
      RenderPassDescriptor$Builder descriptor = RenderPassDescriptor.builder(() -> "OIT Transmittance for " + label.get())
         .withDepthAttachment(params.depthTextureView, OptionalDouble.empty());

      for (int i = 0; i < LevelRenderer.OIT_TRANSMITTANCE_TARGET_COUNT; i++) {
         descriptor.withColorAttachment(params.transmittanceTargetViews[i], Optional.empty());
      }

      GpuSampler nearestSampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
      RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(descriptor.build());
      RenderSystem.bindDefaultUniforms(renderPass);
      renderPass.setUniform("DepthBoundsSampler", params.depthBoundsTargetView, nearestSampler);
      return renderPass;
   }

   private static RenderPass createAccumulatePass(final Supplier<String> label, final OitRenderPassProvider$Parameters params) {
      RenderPassDescriptor descriptor = RenderPassDescriptor.builder(() -> "OIT Accumulate for " + label.get())
         .withColorAttachment(params.accumulateTargetView)
         .withDepthAttachment(params.depthTextureView)
         .build();
      RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(descriptor);
      GpuSampler nearestSampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
      RenderSystem.bindDefaultUniforms(renderPass);

      for (int i = 0; i < LevelRenderer.OIT_TRANSMITTANCE_TARGET_COUNT; i++) {
         renderPass.setUniform("Coeff" + i, params.transmittanceTargetViews[i], nearestSampler);
      }

      renderPass.setUniform("DepthBoundsSampler", params.depthBoundsTargetView, nearestSampler);
      return renderPass;
   }
}
