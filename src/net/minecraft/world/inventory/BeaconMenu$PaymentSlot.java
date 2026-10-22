package net.minecraft.world.inventory;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

class BeaconMenu$PaymentSlot extends Slot {
   public BeaconMenu$PaymentSlot(final Container container, final int slot, final int x, final int y) {
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
   }

   @Override
   public int getMaxStackSize() {
      return 1;
   }
}
