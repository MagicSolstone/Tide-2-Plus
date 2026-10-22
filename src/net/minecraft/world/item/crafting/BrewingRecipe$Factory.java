package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStackTemplate;

@FunctionalInterface
public interface BrewingRecipe$Factory<T extends BrewingRecipe> {
   T create(final Recipe$CommonInfo commonInfo, final PotionIngredient input, final PotionIngredient reagent, final ItemStackTemplate output);
}
