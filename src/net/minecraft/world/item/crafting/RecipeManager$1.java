package net.minecraft.world.item.crafting;

import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.Nullable;

class RecipeManager$1 implements RecipeManager$CachedCheck<I, T> {
   @Nullable
   private ResourceKey<Recipe<?>> lastRecipe;

   RecipeManager$1(final RecipeType val$type) {
      this.val$type = val$typex;
   }

   @Override
   public Optional<RecipeHolder<T>> getRecipeFor(final I input, final ServerLevel level) {
      RecipeManager recipeManager = level.recipeAccess();
      Optional<RecipeHolder<T>> result = recipeManager.getRecipeFor(this.val$type, input, level, this.lastRecipe);
      if (result.isPresent()) {
         RecipeHolder<T> unpackedResult = (RecipeHolder<T>)result.get();
         this.lastRecipe = unpackedResult.id();
         return Optional.of(unpackedResult);
      } else {
         return Optional.empty();
      }
   }
}
