package com.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class TlsfAllocator$Block {
   private long size = 0L;
   public final TlsfAllocator$Heap heap;
   public long offsetFromHeap;
   @Nullable
   public TlsfAllocator$Block nextFreeBlock;
   @Nullable
   public TlsfAllocator$Block previousFreeBlock;
   @Nullable
   public TlsfAllocator$Block nextPhysicalBlock;
   @Nullable
   public TlsfAllocator$Block previousPhysicalBlock;
   private static final int BLOCK_HEADER_FREE_BIT = 1;

   private TlsfAllocator$Block(
      final long size,
      final TlsfAllocator$Heap heap,
      final long offsetFromHeap,
      @Nullable final TlsfAllocator$Block nextFreeBlock,
      @Nullable final TlsfAllocator$Block previousFreeBlock,
      @Nullable final TlsfAllocator$Block nextPhysicalBlock,
      @Nullable final TlsfAllocator$Block previousPhysicalBlock
   ) {
      this.heap = heap;
      this.offsetFromHeap = offsetFromHeap;
      this.nextFreeBlock = nextFreeBlock;
      this.previousFreeBlock = previousFreeBlock;
      this.nextPhysicalBlock = nextPhysicalBlock;
      this.previousPhysicalBlock = previousPhysicalBlock;
      this.setSize(size);
   }

   private boolean isFree() {
      return (this.size & 1L) == 1L;
   }

   private void setFree() {
      this.size |= 1L;
   }

   private void setUsed() {
      this.size &= -2L;
   }

   private long getSize() {
      return this.size & -2L;
   }

   private void setSize(final long size) {
      long oldSize = this.size;
      this.size = size | oldSize & 1L;
   }
}
