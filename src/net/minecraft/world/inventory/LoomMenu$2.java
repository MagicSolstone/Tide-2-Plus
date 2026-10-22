package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.SimpleContainer;

class LoomMenu$2 extends SimpleContainer {
   LoomMenu$2(final LoomMenu this$0, final int size) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(size);
   }

   @Override
   public void setChanged() {
      super.setChanged();
      this.this$0.slotUpdateListener.run();
   }
}
