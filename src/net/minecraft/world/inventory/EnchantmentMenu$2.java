package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.Container;

class EnchantmentMenu$2 extends Slot {
   EnchantmentMenu$2(final EnchantmentMenu this$0, final Container container, final int slot, final int x, final int y) {
      Objects.requireNonNull(this$0);
      super(container, slot, x, y);
   }

   @Override
   public int getMaxStackSize() {
      return 1;
   }
}
