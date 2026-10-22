package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipePropertySet;

class BrewingStandMenu$IngredientsSlot extends Slot {
   private final RecipePropertySet propertySet;

   public BrewingStandMenu$IngredientsSlot(final RecipePropertySet propertySet, final Container container, final int slot, final int x, final int y) {
      super(container, slot, x, y);
      this.propertySet = propertySet;
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return this.propertySet.test(itemStack);
   }
}
