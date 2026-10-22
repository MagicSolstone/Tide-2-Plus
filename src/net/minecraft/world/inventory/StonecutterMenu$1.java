package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.SimpleContainer;

class StonecutterMenu$1 extends SimpleContainer {
   StonecutterMenu$1(final StonecutterMenu this$0, final int size) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(size);
   }

   @Override
   public void setChanged() {
      super.setChanged();
      this.this$0.slotsChanged(this);
      this.this$0.slotUpdateListener.run();
   }
}
