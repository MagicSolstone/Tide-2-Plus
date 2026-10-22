package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.Container;

class LecternMenu$1 extends Slot {
   LecternMenu$1(final LecternMenu this$0, final Container container, final int slot, final int x, final int y) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(container, slot, x, y);
   }

   @Override
   public void setChanged() {
      super.setChanged();
      this.this$0.slotsChanged(this.container);
   }
}
