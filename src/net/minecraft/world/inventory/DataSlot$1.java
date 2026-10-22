package net.minecraft.world.inventory;

class DataSlot$1 extends DataSlot {
   DataSlot$1(final ContainerData val$container, final int val$dataId) {
      this.val$container = val$containerx;
      this.val$dataId = val$dataIdx;
   }

   @Override
   public int get() {
      return this.val$container.get(this.val$dataId);
   }

   @Override
   public void set(final int value) {
      this.val$container.set(this.val$dataId, value);
   }
}
