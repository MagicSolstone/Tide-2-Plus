package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;

class AbstractContainerMenu$1 implements SlotAccess {
   AbstractContainerMenu$1(final AbstractContainerMenu this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ItemStack get() {
      return this.this$0.getCarried();
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      this.this$0.setCarried(itemStack);
      return true;
   }
}
