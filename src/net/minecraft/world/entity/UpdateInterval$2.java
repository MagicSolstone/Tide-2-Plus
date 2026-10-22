package net.minecraft.world.entity;

class UpdateInterval$2 implements UpdateInterval {
   UpdateInterval$2(final int val$period) {
      this.val$period = val$periodx;
   }

   @Override
   public boolean test(final int tick) {
      return tick % this.val$period == 0;
   }

   @Override
   public int nextInterval(final int tick) {
      return tick / this.val$period * this.val$period + this.val$period;
   }
}
