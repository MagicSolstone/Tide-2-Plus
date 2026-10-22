package net.minecraft.core;

import com.google.common.collect.AbstractIterator;
import net.minecraft.util.RandomSource;

class BlockPos$2 extends AbstractIterator<BlockPos> {
   private final BlockPos$MutableBlockPos nextPos;
   private int counter;

   BlockPos$2(
      final int val$limit,
      final int val$minX,
      final RandomSource val$random,
      final int val$width,
      final int val$minY,
      final int val$height,
      final int val$minZ,
      final int val$depth
   ) {
      this.val$limit = val$limitx;
      this.val$minX = val$minXx;
      this.val$random = val$randomx;
      this.val$width = val$widthx;
      this.val$minY = val$minYx;
      this.val$height = val$heightx;
      this.val$minZ = val$minZx;
      this.val$depth = val$depthx;
      this.nextPos = new BlockPos$MutableBlockPos();
      this.counter = this.val$limit;
   }

   protected BlockPos computeNext() {
      if (this.counter <= 0) {
         return (BlockPos)this.endOfData();
      } else {
         BlockPos next = this.nextPos
            .set(
               this.val$minX + this.val$random.nextInt(this.val$width),
               this.val$minY + this.val$random.nextInt(this.val$height),
               this.val$minZ + this.val$random.nextInt(this.val$depth)
            );
         this.counter--;
         return next;
      }
   }
}
