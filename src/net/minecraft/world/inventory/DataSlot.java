package net.minecraft.world.inventory;

public abstract class DataSlot {
   private int prevValue;

   public static DataSlot forContainer(final ContainerData container, final int dataId) {
      return new DataSlot$1(container, dataId);
   }

   public static DataSlot shared(final int[] storage, final int index) {
      return new DataSlot$2(storage, index);
   }

   public static DataSlot standalone() {
      return new DataSlot$3();
   }

   public abstract int get();

   public abstract void set(int value);

   public boolean checkAndClearUpdateFlag() {
      int currentValue = this.get();
      boolean result = currentValue != this.prevValue;
      this.prevValue = currentValue;
      return result;
   }
}
