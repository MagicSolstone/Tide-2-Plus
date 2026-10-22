package net.minecraft.world.inventory;

import java.util.Objects;

class ItemCombinerMenu$1 extends ResultContainer {
   ItemCombinerMenu$1(final ItemCombinerMenu this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void setChanged() {
      this.this$0.slotsChanged(this);
   }
}
