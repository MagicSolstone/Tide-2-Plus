package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

class GrindstoneMenu$3 extends Slot {
   GrindstoneMenu$3(final GrindstoneMenu this$0, final Container container, final int slot, final int x, final int y) {
      Objects.requireNonNull(this$0);
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return itemStack.isDamageableItem() || EnchantmentHelper.hasAnyEnchantments(itemStack);
   }
}
