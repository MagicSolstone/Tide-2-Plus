package net.minecraft.data.recipes;

import java.util.function.BiFunction;
import net.minecraft.world.item.crafting.CraftingRecipe$CraftingBookInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Recipe$CommonInfo;

@FunctionalInterface
public interface CustomCraftingRecipeBuilder$Factory extends BiFunction<Recipe$CommonInfo, CraftingRecipe$CraftingBookInfo, Recipe<?>> {
}
