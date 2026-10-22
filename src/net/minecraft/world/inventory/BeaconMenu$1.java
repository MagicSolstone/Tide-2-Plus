package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

class BeaconMenu$1 extends SimpleContainer {
   BeaconMenu$1(final BeaconMenu this$0, final int size) {
      Objects.requireNonNull(this$0);
      super(size);
   }

   @Override
   public boolean canPlaceItem(final int slot, final ItemStack itemStack) {
      return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
   }

   @Override
   public int getMaxStackSize() {
      return 1;
   }
}
