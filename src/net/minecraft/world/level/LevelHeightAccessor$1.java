package net.minecraft.world.level;

class LevelHeightAccessor$1 implements LevelHeightAccessor {
   LevelHeightAccessor$1(final int val$height, final int val$minY) {
      this.val$height = val$heightx;
      this.val$minY = val$minYx;
   }

   @Override
   public int getHeight() {
      return this.val$height;
   }

   @Override
   public int getMinY() {
      return this.val$minY;
   }
}
