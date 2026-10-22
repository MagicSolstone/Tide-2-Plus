package net.minecraft.world;

import java.util.Objects;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;

class Container$1 implements SlotAccess {
   Container$1(final Container this$0, final int val$slot) {
      this.val$slot = val$slotx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ItemStack get() {
      return this.this$0.getItem(this.val$slot);
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      this.this$0.setItem(this.val$slot, itemStack);
      return true;
   }
}
