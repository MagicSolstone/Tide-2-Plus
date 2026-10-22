package com.mojang.renderpearl.api.device;

import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.Collection;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GpuSurface extends UncheckedAutoCloseable {
   void configure(GpuSurface$Configuration config) throws SurfaceException;

   Optional<GpuSurface$Configuration> currentConfiguration();

   Collection<GpuSurface$PresentMode> supportedPresentModes();

   boolean isSuboptimal();

   boolean isAcquired();

   void acquireNextTexture() throws SurfaceException;

   void blitFromTexture(CommandEncoder commandEncoder, GpuTextureView textureView);

   void present();
}
