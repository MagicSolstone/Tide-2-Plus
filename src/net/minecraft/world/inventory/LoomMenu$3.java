package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.Container;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

class LoomMenu$3 extends Slot {
   LoomMenu$3(final LoomMenu this$0, final Container container, final int slot, final int x, final int y) {
      Objects.requireNonNull(this$0);
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return itemStack.getItem() instanceof BannerItem;
   }
}
