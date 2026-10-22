package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.world.inventory.ContainerData;

class CrafterBlockEntity$1 implements ContainerData {
   private final int[] slotStates;
   private int triggered;

   CrafterBlockEntity$1(final CrafterBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      super();
      this.slotStates = new int[9];
      this.triggered = 0;
   }

   @Override
   public int get(final int dataId) {
      return dataId == 9 ? this.triggered : this.slotStates[dataId];
   }

   @Override
   public void set(final int dataId, final int value) {
      if (dataId == 9) {
         this.triggered = value;
      } else {
         this.slotStates[dataId] = value;
      }
   }

   @Override
   public int getCount() {
      return 10;
   }
}
