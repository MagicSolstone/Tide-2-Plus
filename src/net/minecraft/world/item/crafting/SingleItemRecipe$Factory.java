package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStackTemplate;

@FunctionalInterface
public interface SingleItemRecipe$Factory<T extends SingleItemRecipe> {
   T create(Recipe$CommonInfo commonInfo, Ingredient ingredient, ItemStackTemplate result);
}
