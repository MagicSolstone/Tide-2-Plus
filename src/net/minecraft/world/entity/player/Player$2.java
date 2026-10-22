package net.minecraft.world.entity.player;

import java.util.Objects;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;

class Player$2 implements SlotAccess {
   Player$2(final Player this$0, final int val$craftSlot) {
      this.val$craftSlot = val$craftSlotx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ItemStack get() {
      return this.this$0.inventoryMenu.getCraftSlots().getItem(this.val$craftSlot);
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      this.this$0.inventoryMenu.getCraftSlots().setItem(this.val$craftSlot, itemStack);
      this.this$0.inventoryMenu.slotsChanged(this.this$0.inventory);
      return true;
   }
}
