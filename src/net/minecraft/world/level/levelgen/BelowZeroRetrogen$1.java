package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.LevelHeightAccessor;

class BelowZeroRetrogen$1 implements LevelHeightAccessor {
   @Override
   public int getHeight() {
      return 64;
   }

   @Override
   public int getMinY() {
      return -64;
   }
}
