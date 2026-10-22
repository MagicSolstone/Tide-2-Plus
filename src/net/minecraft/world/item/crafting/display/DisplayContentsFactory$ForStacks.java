package net.minecraft.world.item.crafting.display;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface DisplayContentsFactory$ForStacks<T> extends DisplayContentsFactory<T> {
   default T forStack(final Holder<Item> item) {
      return this.forStack(new ItemStack(item));
   }

   default T forStack(final Item item) {
      return this.forStack(new ItemStack(item));
   }

   T forStack(ItemStack stack);
}
