package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

class ItemCombinerMenu$2 extends Slot {
   ItemCombinerMenu$2(
      final ItemCombinerMenu this$0,
      final Container container,
      final int slot,
      final int x,
      final int y,
      final ItemCombinerMenuSlotDefinition$SlotDefinition val$slot
   ) {
      this.val$slot = val$slotx;
      Objects.requireNonNull(this$0);
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return this.val$slot.mayPlace().test(itemStack);
   }
}
