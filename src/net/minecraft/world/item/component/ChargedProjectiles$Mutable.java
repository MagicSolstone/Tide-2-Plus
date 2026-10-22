package net.minecraft.world.item.component;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ChargedProjectiles$Mutable extends GrowableMutableContainer<ChargedProjectiles> {
   private ChargedProjectiles$Mutable(final List<ItemStack> items) {
      super(items);
   }

   @Override
   protected boolean addSlotWithItem(final ItemProvider newItems) {
      return newItems.findNextNonEmpty() && super.addSlotWithItem(newItems);
   }

   @Override
   public boolean canInsertNewSlots() {
      return this.items.size() < 1024;
   }

   public ChargedProjectiles toImmutable() {
      List<ItemStackTemplate> nonEmptyItems = new ArrayList<>(this.items.size());

      for (ItemStack item : this.items) {
         if (!item.isEmpty()) {
            nonEmptyItems.add(ItemStackTemplate.fromNonEmptyStack(item));
         }
      }

      return new ChargedProjectiles(nonEmptyItems);
   }
}
