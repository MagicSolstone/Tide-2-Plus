package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocator$Block;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record GlTransientMemory$Fallback$GlAllocation(GlBuffer glBuffer, long hostBuffer) implements TransientBlockAllocator$Allocator$Block {
   @Override
   public boolean suboptimal() {
      return false;
   }
}
