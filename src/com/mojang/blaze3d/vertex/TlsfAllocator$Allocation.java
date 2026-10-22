package com.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TlsfAllocator$Allocation {
   private final TlsfAllocator$Block block;
   private final long offsetFromHeap;
   private boolean freed = false;

   private TlsfAllocator$Allocation(final TlsfAllocator$Block block, final long offsetFromHeap) {
      this.block = block;
      this.offsetFromHeap = offsetFromHeap;
   }

   public long getSize() {
      return this.block.getSize();
   }

   public TlsfAllocator$Heap getHeap() {
      return this.block.heap;
   }

   public long getOffsetFromHeap() {
      return this.offsetFromHeap;
   }

   public boolean isFreed() {
      return this.freed;
   }
}
