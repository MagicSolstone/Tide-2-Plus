package com.mojang.renderpearl.backend.api;

import com.mojang.renderpearl.api.device.GpuSurface$Configuration;
import com.mojang.renderpearl.api.device.GpuSurface$PresentMode;
import com.mojang.renderpearl.api.device.SurfaceException;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.Collection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GpuSurfaceBackend extends UncheckedAutoCloseable {
   void configure(GpuSurface$Configuration config) throws SurfaceException;

   boolean isSuboptimal();

   void acquireNextTexture() throws SurfaceException;

   void blitFromTexture(CommandEncoderBackend commandEncoder, GpuTextureView textureView);

   void present();

   Collection<GpuSurface$PresentMode> supportedPresentModes();
}
