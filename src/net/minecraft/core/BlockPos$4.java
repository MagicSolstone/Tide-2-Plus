package net.minecraft.core;

import com.google.common.collect.AbstractIterator;

class BlockPos$4 extends AbstractIterator<BlockPos> {
   private final BlockPos$MutableBlockPos cursor;
   private int index;

   BlockPos$4(final int val$end, final int val$width, final int val$height, final int val$minX, final int val$minY, final int val$minZ) {
      this.val$end = val$endx;
      this.val$width = val$widthx;
      this.val$height = val$heightx;
      this.val$minX = val$minXx;
      this.val$minY = val$minYx;
      this.val$minZ = val$minZx;
      this.cursor = new BlockPos$MutableBlockPos();
   }

   protected BlockPos computeNext() {
      if (this.index == this.val$end) {
         return (BlockPos)this.endOfData();
      } else {
         int x = this.index % this.val$width;
         int slice = this.index / this.val$width;
         int y = slice % this.val$height;
         int z = slice / this.val$height;
         this.index++;
         return this.cursor.set(this.val$minX + x, this.val$minY + y, this.val$minZ + z);
      }
   }
}
