package net.minecraft.world.item.component;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public class ItemContainerContents$Mutable extends GrowableMutableContainer<ItemContainerContents> {
   private ItemContainerContents$Mutable(final List<ItemStack> items) {
      super(items);
   }

   @Override
   public boolean canInsertNewSlots() {
      return this.items.size() < 256;
   }

   public ItemContainerContents toImmutable() {
      return ItemContainerContents.fromItems(this.items);
   }
}
