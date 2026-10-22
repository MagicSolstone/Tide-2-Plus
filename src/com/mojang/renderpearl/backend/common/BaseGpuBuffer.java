package com.mojang.renderpearl.backend.common;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class BaseGpuBuffer implements GpuBuffer {
   @GpuBuffer$Usage
   private final int usage;
   private final long size;

   public BaseGpuBuffer(@GpuBuffer$Usage final int usage, final long size) {
      this.size = size;
      this.usage = usage;
   }

   @Override
   public long size() {
      return this.size;
   }

   @GpuBuffer$Usage
   @Override
   public int usage() {
      return this.usage;
   }

   public void checkCanBeUsed() {
   }
}
