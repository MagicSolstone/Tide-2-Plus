package net.minecraft.client.gui.screens.recipebook;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public record OverlayRecipeComponent$OverlayRecipeButton$Pos(int x, int y, List<ItemStack> ingredients) {
   public OverlayRecipeComponent$OverlayRecipeButton$Pos(int x, int y, List<ItemStack> ingredients) {
      if (ingredients.isEmpty()) {
         throw new IllegalArgumentException("Ingredient list must be non-empty");
      } else {
         this.x = x;
         this.y = y;
         this.ingredients = ingredients;
      }
   }

   public ItemStack selectIngredient(final int currentIndex) {
      return this.ingredients.get(currentIndex % this.ingredients.size());
   }
}
