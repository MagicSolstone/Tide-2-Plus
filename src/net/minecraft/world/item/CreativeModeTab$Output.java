package net.minecraft.world.item;

import java.util.Collection;
import net.minecraft.world.level.ItemLike;

public interface CreativeModeTab$Output {
   void accept(final ItemStack stack, final CreativeModeTab$TabVisibility tabVisibility);

   default void accept(final ItemStack stack) {
      this.accept(stack, CreativeModeTab$TabVisibility.PARENT_AND_SEARCH_TABS);
   }

   default void accept(final ItemLike item, final CreativeModeTab$TabVisibility tabVisibility) {
      this.accept(new ItemStack(item), tabVisibility);
   }

   default void accept(final ItemLike item) {
      this.accept(new ItemStack(item), CreativeModeTab$TabVisibility.PARENT_AND_SEARCH_TABS);
   }

   default void acceptAll(final Collection<ItemStack> stacks, final CreativeModeTab$TabVisibility tabVisibility) {
      stacks.forEach(stack -> this.accept(stack, tabVisibility));
   }

   default void acceptAll(final Collection<ItemStack> stacks) {
      this.acceptAll(stacks, CreativeModeTab$TabVisibility.PARENT_AND_SEARCH_TABS);
   }
}
