package net.minecraft.world.item;

import it.unimi.dsi.fastutil.Hash.Strategy;
import org.jspecify.annotations.Nullable;

class ItemStackLinkedSet$1 implements Strategy<ItemStack> {
   public int hashCode(@Nullable final ItemStack item) {
      return ItemStack.hashItemAndComponents(item);
   }

   public boolean equals(@Nullable final ItemStack a, @Nullable final ItemStack b) {
      return a == b || a != null && b != null && a.isEmpty() == b.isEmpty() && ItemStack.isSameItemSameComponents(a, b);
   }
}
