package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.Optional;
import java.util.OptionalDouble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class RenderTarget {
   private static int UNNAMED_RENDER_TARGETS = 0;
   public int width;
   public int height;
   protected final String label;
   @Nullable
   protected final GpuFormat colorFormat;
   @Nullable
   protected final GpuFormat depthFormat;
   @Nullable
   protected GpuTexture colorTexture;
   @Nullable
   protected GpuTextureView colorTextureView;
   @Nullable
   protected GpuTexture depthTexture;
   @Nullable
   protected GpuTextureView depthTextureView;

   public RenderTarget(@Nullable final String label, @Nullable final GpuFormat colorFormat, @Nullable final GpuFormat depthFormat) {
      this.label = label == null ? "FBO " + UNNAMED_RENDER_TARGETS++ : label;
      this.colorFormat = colorFormat;
      this.depthFormat = depthFormat;
   }

   public void resize(final int width, final int height) {
      RenderSystem.assertOnRenderThread();
      this.destroyBuffers();
      this.createBuffers(width, height);
   }

   public void destroyBuffers() {
      RenderSystem.assertOnRenderThread();
      if (this.depthTexture != null) {
         this.depthTexture.close();
         this.depthTexture = null;
      }

      if (this.depthTextureView != null) {
         this.depthTextureView.close();
         this.depthTextureView = null;
      }

      if (this.colorTexture != null) {
         this.colorTexture.close();
         this.colorTexture = null;
      }

      if (this.colorTextureView != null) {
         this.colorTextureView.close();
         this.colorTextureView = null;
      }
   }

   public void copyDepthFrom(final RenderTarget source) {
      RenderSystem.assertOnRenderThread();
      if (this.depthTexture == null) {
         throw new IllegalStateException("Trying to copy depth texture to a RenderTarget without a depth texture");
      } else if (source.depthTexture == null) {
         throw new IllegalStateException("Trying to copy depth texture from a RenderTarget without a depth texture");
      } else {
         RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(source.depthTexture, this.depthTexture, 0, 0, 0, 0, 0, this.width, this.height);
      }
   }

   public void copyColorFrom(final RenderTarget source) {
      RenderSystem.assertOnRenderThread();
      if (this.colorTexture == null) {
         throw new IllegalStateException("Trying to copy color texture to a RenderTarget without a color texture");
      } else if (source.colorTexture == null) {
         throw new IllegalStateException("Trying to copy color texture from a RenderTarget without a color texture");
      } else {
         RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(source.colorTexture, this.colorTexture, 0, 0, 0, 0, 0, this.width, this.height);
      }
   }

   public void createBuffers(final int width, final int height) {
      RenderSystem.assertOnRenderThread();
      GpuDevice device = RenderSystem.getDevice();
      int maxTextureSize = device.getDeviceInfo().limits().maxTextureSize();
      if (width > 0 && width <= maxTextureSize && height > 0 && height <= maxTextureSize) {
         this.width = width;
         this.height = height;
         if (this.depthFormat != null) {
            this.depthTexture = device.createTexture(() -> this.label + " / Depth", 15, this.depthFormat, width, height, 1, 1);
            this.depthTextureView = device.createTextureView(this.depthTexture);
         }

         if (this.colorFormat != null) {
            this.colorTexture = device.createTexture(() -> this.label + " / Color", 15, this.colorFormat, width, height, 1, 1);
            this.colorTextureView = device.createTextureView(this.colorTexture);
         }
      } else {
         throw new IllegalArgumentException("Window " + width + "x" + height + " size out of bounds (max. size: " + maxTextureSize + ")");
      }
   }

   public void blitAndBlendToTexture(final GpuTextureView output, final GpuTextureView outputDepth) {
      RenderSystem.assertOnRenderThread();

      try (RenderPass renderPass = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Blit render target", output, Optional.empty(), outputDepth, OptionalDouble.empty())) {
         renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.ENTITY_OUTLINE_BLIT));
         RenderSystem.bindDefaultUniforms(renderPass);
         renderPass.setUniform("InSampler", this.colorTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.draw(3, 1, 0, 0);
      }
   }

   @Nullable
   public GpuTexture getColorTexture() {
      return this.colorTexture;
   }

   @Nullable
   public GpuTextureView getColorTextureView() {
      return this.colorTextureView;
   }

   @Nullable
   public GpuTexture getDepthTexture() {
      return this.depthTexture;
   }

   @Nullable
   public GpuTextureView getDepthTextureView() {
      return this.depthTextureView;
   }

   public boolean hasDepth() {
      return this.depthFormat != null;
   }
}
