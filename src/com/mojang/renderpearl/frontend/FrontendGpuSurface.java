package com.mojang.renderpearl.frontend;

import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuSurface;
import com.mojang.renderpearl.api.device.GpuSurface$Configuration;
import com.mojang.renderpearl.api.device.GpuSurface$PresentMode;
import com.mojang.renderpearl.api.device.SurfaceException;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.backend.api.GpuSurfaceBackend;
import java.util.Collection;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FrontendGpuSurface implements GpuSurface {
   private final GpuSurfaceBackend backend;
   private boolean hasImageAcquired = false;
   private boolean hasBlittedTexture = false;
   private Optional<GpuSurface$Configuration> currentConfiguration = Optional.empty();

   public FrontendGpuSurface(final GpuSurfaceBackend backend) {
      this.backend = backend;
   }

   @Override
   public void close() {
      if (this.hasImageAcquired) {
         throw new IllegalStateException("Cannot close a surface while it is acquired");
      } else {
         this.backend.close();
      }
   }

   @Override
   public void configure(final GpuSurface$Configuration config) throws SurfaceException {
      if (this.hasImageAcquired) {
         throw new IllegalStateException("Cannot configure a surface while it is acquired");
      } else if (!this.supportedPresentModes().contains(config.presentMode())) {
         throw new SurfaceException("Surface does not support present mode " + config.presentMode() + " (supported: " + this.supportedPresentModes() + ")");
      } else {
         this.backend.configure(config);
         this.currentConfiguration = Optional.of(config);
      }
   }

   @Override
   public Optional<GpuSurface$Configuration> currentConfiguration() {
      return this.currentConfiguration;
   }

   @Override
   public Collection<GpuSurface$PresentMode> supportedPresentModes() {
      return this.backend.supportedPresentModes();
   }

   @Override
   public boolean isSuboptimal() {
      return this.backend.isSuboptimal();
   }

   @Override
   public boolean isAcquired() {
      return this.hasImageAcquired;
   }

   @Override
   public void acquireNextTexture() throws SurfaceException {
      if (this.hasImageAcquired) {
         throw new IllegalStateException("Cannot acquire a surface while it is already acquired");
      } else if (this.currentConfiguration.isEmpty()) {
         throw new IllegalStateException("Cannot acquire an unconfigured surface");
      } else {
         this.backend.acquireNextTexture();
         this.hasImageAcquired = true;
         this.hasBlittedTexture = false;
      }
   }

   @Override
   public void blitFromTexture(final CommandEncoder commandEncoder, final GpuTextureView textureView) {
      if (commandEncoder instanceof FrontendCommandEncoder frontendCommandEncoder) {
         if (frontendCommandEncoder.isInRenderPass()) {
            throw new IllegalStateException("Close the existing render pass before presenting with a command encoder");
         } else if (!textureView.texture().getFormat().hasColorAspect()) {
            throw new IllegalStateException("Cannot present a non-color texture!");
         } else if ((textureView.texture().usage() & 2) == 0) {
            throw new IllegalStateException("Color texture must have USAGE_COPY_SRC to presented to the screen");
         } else if (textureView.texture().getDepthOrLayers() > 1) {
            throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for presentation");
         } else if (!this.hasImageAcquired) {
            throw new IllegalStateException("Cannot present to an unacquired surface");
         } else if (this.hasBlittedTexture) {
            throw new IllegalStateException("Already blitted to this frame!");
         } else {
            this.backend.blitFromTexture(frontendCommandEncoder.backend(), textureView);
            this.hasBlittedTexture = true;
         }
      } else {
         throw new IllegalArgumentException("CommandEncoder must be instance of FrontendCommandEncoder");
      }
   }

   @Override
   public void present() {
      if (!this.hasImageAcquired) {
         throw new IllegalStateException("Cannot present to a surface if it isn't acquired");
      } else if (!this.hasBlittedTexture) {
         throw new IllegalStateException("Must blit to surface before presenting!");
      } else {
         this.backend.present();
         this.hasImageAcquired = false;
      }
   }
}
