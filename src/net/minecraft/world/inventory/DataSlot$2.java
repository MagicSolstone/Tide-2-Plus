package net.minecraft.world.inventory;

class DataSlot$2 extends DataSlot {
   DataSlot$2(final int[] val$storage, final int val$index) {
      this.val$storage = val$storagex;
      this.val$index = val$indexx;
   }

   @Override
   public int get() {
      return this.val$storage[this.val$index];
   }

   @Override
   public void set(final int value) {
      this.val$storage[this.val$index] = value;
   }
}
