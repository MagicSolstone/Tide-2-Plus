package com.mojang.renderpearl.backend.opengl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record GlTransientMemory$PersistentMapping$Rotation(Runnable staging, Runnable gpu, Runnable gpuMapped) implements Runnable {
   @Override
   public void run() {
      this.staging.run();
      this.gpu.run();
      this.gpuMapped.run();
   }
}
