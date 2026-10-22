package net.minecraft.world.inventory;

import java.util.Objects;

class CartographyTableMenu$2 extends ResultContainer {
   CartographyTableMenu$2(final CartographyTableMenu this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void setChanged() {
      this.this$0.slotsChanged(this);
      super.setChanged();
   }
}
