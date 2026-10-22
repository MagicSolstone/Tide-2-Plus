package net.minecraft.world.entity;

import java.util.List;
import net.minecraft.world.item.ItemStack;

class SlotAccess$3 implements SlotAccess {
   SlotAccess$3(final List val$stacks, final int val$index) {
      this.val$stacks = val$stacksx;
      this.val$index = val$indexx;
   }

   @Override
   public ItemStack get() {
      return (ItemStack)this.val$stacks.get(this.val$index);
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      this.val$stacks.set(this.val$index, itemStack);
      return true;
   }
}
