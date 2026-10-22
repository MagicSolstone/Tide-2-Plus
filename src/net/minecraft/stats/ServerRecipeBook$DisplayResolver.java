package net.minecraft.stats;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

@FunctionalInterface
public interface ServerRecipeBook$DisplayResolver {
   void displaysForRecipe(ResourceKey<Recipe<?>> id, Consumer<RecipeDisplayEntry> output);
}
