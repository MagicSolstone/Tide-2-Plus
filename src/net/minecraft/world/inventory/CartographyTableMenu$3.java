package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

class CartographyTableMenu$3 extends Slot {
   CartographyTableMenu$3(final CartographyTableMenu this$0, final Container container, final int slot, final int x, final int y) {
      Objects.requireNonNull(this$0);
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return itemStack.has(DataComponents.MAP_ID);
   }
}
