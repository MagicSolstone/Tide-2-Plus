package net.minecraft.world.inventory;

import it.unimi.dsi.fastutil.ints.IntList;

class SlotRange$1 implements SlotRange {
   SlotRange$1(final IntList val$slots, final String val$name) {
      this.val$slots = val$slotsx;
      this.val$name = val$namex;
   }

   @Override
   public IntList slots() {
      return this.val$slots;
   }

   @Override
   public String getSerializedName() {
      return this.val$name;
   }

   @Override
   public String toString() {
      return this.val$name;
   }
}
