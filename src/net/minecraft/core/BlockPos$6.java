package net.minecraft.core;

import com.google.common.collect.AbstractIterator;

class BlockPos$6 extends AbstractIterator<BlockPos$MutableBlockPos> {
   private final Direction[] directions;
   private final BlockPos$MutableBlockPos cursor;
   private final int legs;
   private int leg;
   private int legSize;
   private int legIndex;
   private int lastX;
   private int lastY;
   private int lastZ;

   BlockPos$6(final Direction val$firstDirection, final Direction val$secondDirection, final BlockPos val$center, final int val$radius) {
      this.val$firstDirection = val$firstDirectionx;
      this.val$secondDirection = val$secondDirectionx;
      this.val$center = val$centerx;
      this.val$radius = val$radiusx;
      this.directions = new Direction[]{
         this.val$firstDirection, this.val$secondDirection, this.val$firstDirection.getOpposite(), this.val$secondDirection.getOpposite()
      };
      this.cursor = this.val$center.mutable().move(this.val$secondDirection);
      this.legs = 4 * this.val$radius;
      this.leg = -1;
      this.lastX = this.cursor.getX();
      this.lastY = this.cursor.getY();
      this.lastZ = this.cursor.getZ();
   }

   protected BlockPos$MutableBlockPos computeNext() {
      this.cursor.set(this.lastX, this.lastY, this.lastZ).move(this.directions[(this.leg + 4) % 4]);
      this.lastX = this.cursor.getX();
      this.lastY = this.cursor.getY();
      this.lastZ = this.cursor.getZ();
      if (this.legIndex >= this.legSize) {
         if (this.leg >= this.legs) {
            return (BlockPos$MutableBlockPos)this.endOfData();
         }

         this.leg++;
         this.legIndex = 0;
         this.legSize = this.leg / 2 + 1;
      }

      this.legIndex++;
      return this.cursor;
   }
}
