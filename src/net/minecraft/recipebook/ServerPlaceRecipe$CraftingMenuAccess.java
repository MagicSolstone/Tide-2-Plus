package net.minecraft.recipebook;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public interface ServerPlaceRecipe$CraftingMenuAccess<T extends Recipe<?>> {
   void fillCraftSlotsStackedContents(StackedItemContents stackedContents);

   void clearCraftingContent();

   boolean recipeMatches(RecipeHolder<T> recipe);
}
