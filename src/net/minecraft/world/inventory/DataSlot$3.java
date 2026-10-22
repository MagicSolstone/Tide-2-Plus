package net.minecraft.world.inventory;

class DataSlot$3 extends DataSlot {
   private int value;

   @Override
   public int get() {
      return this.value;
   }

   @Override
   public void set(final int value) {
      this.value = value;
   }
}
