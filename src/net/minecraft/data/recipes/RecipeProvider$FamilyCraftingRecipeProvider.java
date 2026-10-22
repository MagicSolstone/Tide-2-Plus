package net.minecraft.data.recipes;

import net.minecraft.world.level.ItemLike;

@FunctionalInterface
interface RecipeProvider$FamilyCraftingRecipeProvider {
   RecipeBuilder create(RecipeProvider context, ItemLike result, ItemLike base);
}
