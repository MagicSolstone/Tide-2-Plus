package net.minecraft.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

record RecipeCache$Entry(NonNullList<ItemStack> key, int width, int height, @Nullable RecipeHolder<CraftingRecipe> value) {
   public boolean matches(final CraftingInput input) {
      if (this.width == input.width() && this.height == input.height()) {
         for (int i = 0; i < this.key.size(); i++) {
            if (!ItemStack.isSameItemSameComponents(this.key.get(i), input.getItem(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
