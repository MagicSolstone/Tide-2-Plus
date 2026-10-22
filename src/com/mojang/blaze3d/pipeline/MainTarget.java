package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.device.GpuOutOfMemoryException;
import com.mojang.renderpearl.api.textures.GpuTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class MainTarget extends RenderTarget {
   public static final int DEFAULT_WIDTH = 854;
   public static final int DEFAULT_HEIGHT = 480;
   private static final MainTarget$Dimension DEFAULT_DIMENSIONS = new MainTarget$Dimension(854, 480);

   public MainTarget(final int desiredWidth, final int desiredHeight) {
      super("Main", GpuFormat.RGBA8_UNORM, GpuFormat.D32_FLOAT);
      this.createFrameBuffer(desiredWidth, desiredHeight);
   }

   private void createFrameBuffer(final int desiredWidth, final int desiredHeight) {
      MainTarget$Dimension allocatedDimensions = this.allocateAttachments(desiredWidth, desiredHeight);
      if (this.colorTexture != null && this.depthTexture != null) {
         this.width = allocatedDimensions.width;
         this.height = allocatedDimensions.height;
      } else {
         throw new IllegalStateException("Missing color and/or depth textures");
      }
   }

   private MainTarget$Dimension allocateAttachments(final int width, final int height) {
      RenderSystem.assertOnRenderThread();

      for (MainTarget$Dimension dimension : MainTarget$Dimension.listWithFallback(width, height)) {
         if (this.colorTexture != null) {
            this.colorTexture.close();
            this.colorTexture = null;
         }

         if (this.colorTextureView != null) {
            this.colorTextureView.close();
            this.colorTextureView = null;
         }

         if (this.depthTexture != null) {
            this.depthTexture.close();
            this.depthTexture = null;
         }

         if (this.depthTextureView != null) {
            this.depthTextureView.close();
            this.depthTextureView = null;
         }

         this.colorTexture = this.allocateColorAttachment(dimension);
         this.depthTexture = this.allocateDepthAttachment(dimension);
         if (this.colorTexture != null && this.depthTexture != null) {
            this.colorTextureView = RenderSystem.getDevice().createTextureView(this.colorTexture);
            this.depthTextureView = RenderSystem.getDevice().createTextureView(this.depthTexture);
            return dimension;
         }
      }

      throw new RuntimeException(
         "Unrecoverable GL_OUT_OF_MEMORY ("
            + (this.colorTexture == null ? "missing color" : "have color")
            + ", "
            + (this.depthTexture == null ? "missing depth" : "have depth")
            + ")"
      );
   }

   @Nullable
   private GpuTexture allocateColorAttachment(final MainTarget$Dimension dimension) {
      try {
         return RenderSystem.getDevice().createTexture(() -> this.label + " / Color", 15, this.colorFormat, dimension.width, dimension.height, 1, 1);
      } catch (GpuOutOfMemoryException var3) {
         return null;
      }
   }

   @Nullable
   private GpuTexture allocateDepthAttachment(final MainTarget$Dimension dimension) {
      try {
         return RenderSystem.getDevice().createTexture(() -> this.label + " / Depth", 15, this.depthFormat, dimension.width, dimension.height, 1, 1);
      } catch (GpuOutOfMemoryException var3) {
         return null;
      }
   }
}
