package com.mojang.renderpearl.api.device;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface GpuBackend {
   String getName();

   void loadLibrary() throws BackendCreationException;

   void unloadLibrary();

   long createWindow(@Nullable String title, int width, int height, long flags);

   GpuDevice createDevice(GpuDebugOptions debugOptions) throws BackendCreationException;
}
