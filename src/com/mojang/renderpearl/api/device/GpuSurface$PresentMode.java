package com.mojang.renderpearl.api.device;

import java.util.Collection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum GpuSurface$PresentMode {
   IMMEDIATE,
   MAILBOX,
   FIFO,
   FIFO_RELAXED;

   private static final GpuSurface$PresentMode[] PRESENT_MODES_VSYNC = new GpuSurface$PresentMode[]{FIFO};
   private static final GpuSurface$PresentMode[] PRESENT_MODES_NO_VSYNC = new GpuSurface$PresentMode[]{IMMEDIATE, MAILBOX, FIFO};

   public static GpuSurface$PresentMode getSupportedVsyncMode(final Collection<GpuSurface$PresentMode> supportedModes, final boolean vsync) {
      GpuSurface$PresentMode[] preferred = vsync ? PRESENT_MODES_VSYNC : PRESENT_MODES_NO_VSYNC;

      for (GpuSurface$PresentMode mode : preferred) {
         if (supportedModes.contains(mode)) {
            return mode;
         }
      }

      throw new IllegalStateException("No supported presentation mode was found");
   }
}
