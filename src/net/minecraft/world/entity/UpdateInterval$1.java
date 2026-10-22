package net.minecraft.world.entity;

class UpdateInterval$1 implements UpdateInterval {
   @Override
   public boolean test(final int tick) {
      return false;
   }

   @Override
   public int nextInterval(final int tick) {
      return 0;
   }
}
