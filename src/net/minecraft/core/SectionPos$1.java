package net.minecraft.core;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;

class SectionPos$1 extends AbstractSpliterator<SectionPos> {
   private final Cursor3D cursor;

   SectionPos$1(
      long est,
      int additionalCharacteristics,
      final int val$minX,
      final int val$minY,
      final int val$minZ,
      final int val$maxX,
      final int val$maxY,
      final int val$maxZ
   ) {
      super(est, additionalCharacteristics);
      this.val$minX = val$minXx;
      this.val$minY = val$minYx;
      this.val$minZ = val$minZx;
      this.val$maxX = val$maxXx;
      this.val$maxY = val$maxYx;
      this.val$maxZ = val$maxZx;
      this.cursor = new Cursor3D(this.val$minX, this.val$minY, this.val$minZ, this.val$maxX, this.val$maxY, this.val$maxZ);
   }

   @Override
   public boolean tryAdvance(final Consumer<? super SectionPos> action) {
      if (this.cursor.advance()) {
         action.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ()));
         return true;
      } else {
         return false;
      }
   }
}
