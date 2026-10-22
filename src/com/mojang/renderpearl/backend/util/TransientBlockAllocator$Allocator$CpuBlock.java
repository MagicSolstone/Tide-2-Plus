package com.mojang.renderpearl.backend.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public record TransientBlockAllocator$Allocator$CpuBlock(long address) implements TransientBlockAllocator$Allocator$Block {
   public TransientBlockAllocator$Allocator$CpuBlock(long address) {
      if (address == 0L) {
         throw new IllegalArgumentException("Failed to allocate CPU block");
      } else {
         this.address = address;
      }
   }

   @Override
   public boolean suboptimal() {
      return false;
   }

   public static TransientBlockAllocator$Allocator<TransientBlockAllocator$Allocator$CpuBlock> memalloc() {
      return TransientBlockAllocator$Allocator.create(
         size -> new TransientBlockAllocator$Allocator$CpuBlock(MemoryUtil.nmemAlloc(size)), block -> MemoryUtil.nmemFree(block.address)
      );
   }
}
