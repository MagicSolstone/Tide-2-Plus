package net.minecraft.client.gui.screens.recipebook;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

@Environment(EnvType.CLIENT)
record RecipeButton$ResolvedEntry(RecipeDisplayId id, List<ItemStack> displayItems) {
   public ItemStack selectItem(final int index) {
      if (this.displayItems.isEmpty()) {
         return ItemStack.EMPTY;
      } else {
         int offset = index % this.displayItems.size();
         return this.displayItems.get(offset);
      }
   }
}
