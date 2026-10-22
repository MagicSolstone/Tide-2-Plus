package com.mojang.renderpearl.backend.opengl;

import com.mojang.jtracy.MemoryPool;
import com.mojang.jtracy.TracyClient;
import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.backend.common.BaseGpuBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class GlBuffer extends BaseGpuBuffer {
   protected static final MemoryPool MEMORY_POOL = TracyClient.createMemoryPool("GPU Buffers");
   private final int handle;
   protected final boolean canPersistentMap;
   protected int mappingRefCount = 0;

   protected GlBuffer(@GpuBuffer$Usage final int usage, final long size, final int handle, final boolean canPersistentMap) {
      super(usage, size);
      this.handle = handle;
      this.canPersistentMap = canPersistentMap;
   }

   public int handle() {
      return this.handle;
   }

   @Override
   public void checkCanBeUsed() {
      if (!this.canPersistentMap) {
         if (this.mappingRefCount != 0) {
            throw new IllegalStateException("Attempt to use buffer while mapped without persistent mapping capability");
         }
      }
   }
}
