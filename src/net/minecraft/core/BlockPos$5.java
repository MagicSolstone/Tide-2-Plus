package net.minecraft.core;

import com.google.common.collect.AbstractIterator;

class BlockPos$5 extends AbstractIterator<BlockPos> {
   private final BlockPos$MutableBlockPos cursor;
   private int index;

   BlockPos$5(
      final int val$stepCount,
      final int val$height,
      final Vec3i[] val$steps,
      final int val$startX,
      final int val$startY,
      final int val$yDirection,
      final int val$startZ
   ) {
      this.val$stepCount = val$stepCountx;
      this.val$height = val$heightx;
      this.val$steps = val$stepsx;
      this.val$startX = val$startXx;
      this.val$startY = val$startYx;
      this.val$yDirection = val$yDirectionx;
      this.val$startZ = val$startZx;
      this.cursor = new BlockPos$MutableBlockPos();
   }

   protected BlockPos computeNext() {
      if (this.index == this.val$stepCount) {
         return (BlockPos)this.endOfData();
      } else {
         int y = this.index % this.val$height;
         int stepIndex = this.index / this.val$height;
         Vec3i step = this.val$steps[stepIndex];
         this.index++;
         return this.cursor.set(this.val$startX + step.getX(), this.val$startY + y * this.val$yDirection, this.val$startZ + step.getZ());
      }
   }
}
