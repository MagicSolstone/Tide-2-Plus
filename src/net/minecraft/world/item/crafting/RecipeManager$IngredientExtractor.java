package net.minecraft.world.item.crafting;

import java.util.Optional;

@FunctionalInterface
public interface RecipeManager$IngredientExtractor {
   Optional<Ingredient> apply(Recipe<?> recipe);
}
