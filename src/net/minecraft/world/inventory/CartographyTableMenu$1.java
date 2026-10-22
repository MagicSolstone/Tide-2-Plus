package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.SimpleContainer;

class CartographyTableMenu$1 extends SimpleContainer {
   CartographyTableMenu$1(final CartographyTableMenu this$0, final int size) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(size);
   }

   @Override
   public void setChanged() {
      this.this$0.slotsChanged(this);
      super.setChanged();
   }
}
