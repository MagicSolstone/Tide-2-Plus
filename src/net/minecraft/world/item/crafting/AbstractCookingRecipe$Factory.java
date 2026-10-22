package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStackTemplate;

@FunctionalInterface
public interface AbstractCookingRecipe$Factory<T extends AbstractCookingRecipe> {
   T create(
      Recipe$CommonInfo commonInfo,
      AbstractCookingRecipe$CookingBookInfo cbookInfotegory,
      Ingredient ingredient,
      ItemStackTemplate result,
      float experience,
      int cookingTime
   );
}
