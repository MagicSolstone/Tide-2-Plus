package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

class EnchantmentMenu$3 extends Slot {
   EnchantmentMenu$3(final EnchantmentMenu this$0, final Container container, final int slot, final int x, final int y) {
      Objects.requireNonNull(this$0);
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return itemStack.is(Items.LAPIS_LAZULI);
   }

   @Override
   public Identifier getNoItemIcon() {
      return EnchantmentMenu.EMPTY_SLOT_LAPIS_LAZULI;
   }
}
