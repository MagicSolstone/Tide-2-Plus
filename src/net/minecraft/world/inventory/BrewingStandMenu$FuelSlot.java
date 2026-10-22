package net.minecraft.world.inventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

class BrewingStandMenu$FuelSlot extends Slot {
   public BrewingStandMenu$FuelSlot(final Container container, final int slot, final int x, final int y) {
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return mayPlaceItem(itemStack);
   }

   public static boolean mayPlaceItem(final ItemStack itemStack) {
      return itemStack.has(DataComponents.BREWING_FUEL);
   }

   @Override
   public Identifier getNoItemIcon() {
      return BrewingStandMenu.EMPTY_SLOT_FUEL;
   }
}
