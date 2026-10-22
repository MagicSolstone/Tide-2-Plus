package net.minecraft.core;

import com.google.common.collect.AbstractIterator;

class BlockPos$7 extends AbstractIterator<BlockPos> {
   private final BlockPos$MutableBlockPos cursor;
   private int firstIndex;
   private int secondIndex;
   private int thirdIndex;
   private boolean end;
   private final int firstDirX;
   private final int firstDirY;
   private final int firstDirZ;
   private final int secondDirX;
   private final int secondDirY;
   private final int secondDirZ;
   private final int thirdDirX;
   private final int thirdDirY;
   private final int thirdDirZ;

   BlockPos$7(
      final Direction val$firstVisitDir,
      final Direction val$secondVisitDir,
      final Direction val$thirdVisitDir,
      final int val$startCornerX,
      final int val$startCornerY,
      final int val$startCornerZ,
      final int val$thirdMax,
      final int val$secondMax,
      final int val$firstMax
   ) {
      this.val$firstVisitDir = val$firstVisitDirx;
      this.val$secondVisitDir = val$secondVisitDirx;
      this.val$thirdVisitDir = val$thirdVisitDirx;
      this.val$startCornerX = val$startCornerXx;
      this.val$startCornerY = val$startCornerYx;
      this.val$startCornerZ = val$startCornerZx;
      this.val$thirdMax = val$thirdMaxx;
      this.val$secondMax = val$secondMaxx;
      this.val$firstMax = val$firstMaxx;
      this.cursor = new BlockPos$MutableBlockPos();
      this.firstDirX = this.val$firstVisitDir.getStepX();
      this.firstDirY = this.val$firstVisitDir.getStepY();
      this.firstDirZ = this.val$firstVisitDir.getStepZ();
      this.secondDirX = this.val$secondVisitDir.getStepX();
      this.secondDirY = this.val$secondVisitDir.getStepY();
      this.secondDirZ = this.val$secondVisitDir.getStepZ();
      this.thirdDirX = this.val$thirdVisitDir.getStepX();
      this.thirdDirY = this.val$thirdVisitDir.getStepY();
      this.thirdDirZ = this.val$thirdVisitDir.getStepZ();
   }

   protected BlockPos computeNext() {
      if (this.end) {
         return (BlockPos)this.endOfData();
      } else {
         this.cursor
            .set(
               this.val$startCornerX + this.firstDirX * this.firstIndex + this.secondDirX * this.secondIndex + this.thirdDirX * this.thirdIndex,
               this.val$startCornerY + this.firstDirY * this.firstIndex + this.secondDirY * this.secondIndex + this.thirdDirY * this.thirdIndex,
               this.val$startCornerZ + this.firstDirZ * this.firstIndex + this.secondDirZ * this.secondIndex + this.thirdDirZ * this.thirdIndex
            );
         if (this.thirdIndex < this.val$thirdMax) {
            this.thirdIndex++;
         } else if (this.secondIndex < this.val$secondMax) {
            this.secondIndex++;
            this.thirdIndex = 0;
         } else if (this.firstIndex < this.val$firstMax) {
            this.firstIndex++;
            this.thirdIndex = 0;
            this.secondIndex = 0;
         } else {
            this.end = true;
         }

         return this.cursor;
      }
   }
}
