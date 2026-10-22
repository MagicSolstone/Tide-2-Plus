package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.device.GpuSurface$Configuration;
import com.mojang.renderpearl.api.device.GpuSurface$PresentMode;
import com.mojang.renderpearl.api.device.SurfaceException;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.backend.api.CommandEncoderBackend;
import com.mojang.renderpearl.backend.api.GpuSurfaceBackend;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.sdl.SDLVideo;

@Environment(EnvType.CLIENT)
public class GlSurface implements GpuSurfaceBackend {
   private static final Set<GpuSurface$PresentMode> SUPPORTED_PRESENT_MODES = EnumSet.of(GpuSurface$PresentMode.FIFO, GpuSurface$PresentMode.IMMEDIATE);
   private final GlDevice device;
   private final long windowHandle;
   private final BooleanSupplier isIconified;
   private int swapchainWidth;
   private int swapchainHeight;

   GlSurface(final GlDevice device, final long windowHandle, final BooleanSupplier isIconified) {
      this.device = device;
      this.windowHandle = windowHandle;
      this.isIconified = isIconified;
   }

   @Override
   public void configure(final GpuSurface$Configuration config) throws SurfaceException {
      this.device.makeCurrent(this.windowHandle);
      SDLVideo.SDL_GL_SetSwapInterval(config.presentMode() == GpuSurface$PresentMode.FIFO ? 1 : 0);
      this.swapchainWidth = config.width();
      this.swapchainHeight = config.height();
   }

   @Override
   public boolean isSuboptimal() {
      return false;
   }

   @Override
   public void acquireNextTexture() throws SurfaceException {
      if (this.isIconified.getAsBoolean()) {
         throw new SurfaceException("Cannot acquire minimized window");
      }
   }

   @Override
   public void blitFromTexture(final CommandEncoderBackend commandEncoder, final GpuTextureView textureView) {
      ((GlCommandEncoder)commandEncoder).presentTexture(this.windowHandle, textureView, this.swapchainWidth, this.swapchainHeight);
   }

   @Override
   public void present() {
      SDLVideo.SDL_GL_SwapWindow(this.windowHandle);
   }

   @Override
   public void close() {
   }

   @Override
   public Collection<GpuSurface$PresentMode> supportedPresentModes() {
      return SUPPORTED_PRESENT_MODES;
   }
}
