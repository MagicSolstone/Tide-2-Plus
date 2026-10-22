package net.minecraft.core;

import com.google.common.collect.AbstractIterator;
import org.jspecify.annotations.Nullable;

class BlockPos$3 extends AbstractIterator<BlockPos> {
   private final BlockPos$MutableBlockPos cursor;
   private int currentDepth;
   private int maxX;
   private int maxY;
   private int x;
   private int y;
   private boolean zMirror;

   BlockPos$3(
      final int val$originZ,
      final int val$maxDepth,
      final int val$reachX,
      final int val$reachY,
      final int val$reachZ,
      final int val$originX,
      final int val$originY
   ) {
      this.val$originZ = val$originZx;
      this.val$maxDepth = val$maxDepthx;
      this.val$reachX = val$reachXx;
      this.val$reachY = val$reachYx;
      this.val$reachZ = val$reachZx;
      this.val$originX = val$originXx;
      this.val$originY = val$originYx;
      this.cursor = new BlockPos$MutableBlockPos();
   }

   @Nullable
   protected BlockPos computeNext() {
      if (this.zMirror) {
         this.zMirror = false;
         this.cursor.setZ(this.val$originZ - (this.cursor.getZ() - this.val$originZ));
         return this.cursor;
      } else {
         BlockPos found;
         for (found = null; found == null; this.y++) {
            if (this.y > this.maxY) {
               this.x++;
               if (this.x > this.maxX) {
                  this.currentDepth++;
                  if (this.currentDepth > this.val$maxDepth) {
                     return (BlockPos)this.endOfData();
                  }

                  this.maxX = Math.min(this.val$reachX, this.currentDepth);
                  this.x = -this.maxX;
               }

               this.maxY = Math.min(this.val$reachY, this.currentDepth - Math.abs(this.x));
               this.y = -this.maxY;
            }

            int xx = this.x;
            int yy = this.y;
            int zz = this.currentDepth - Math.abs(xx) - Math.abs(yy);
            if (zz <= this.val$reachZ) {
               this.zMirror = zz != 0;
               found = this.cursor.set(this.val$originX + xx, this.val$originY + yy, this.val$originZ + zz);
            }
         }

         return found;
      }
   }
}
