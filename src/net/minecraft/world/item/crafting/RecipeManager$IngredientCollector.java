package net.minecraft.world.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;

public class RecipeManager$IngredientCollector implements Consumer<Recipe<?>> {
   private final ResourceKey<RecipePropertySet> key;
   private final RecipeManager$IngredientExtractor extractor;
   private final List<Ingredient> ingredients = new ArrayList<>();

   protected RecipeManager$IngredientCollector(final ResourceKey<RecipePropertySet> key, final RecipeManager$IngredientExtractor extractor) {
      this.key = key;
      this.extractor = extractor;
   }

   public void accept(final Recipe<?> recipe) {
      this.extractor.apply(recipe).ifPresent(this.ingredients::add);
   }

   public RecipePropertySet asPropertySet(final FeatureFlagSet enabledFeatures) {
      return RecipePropertySet.create(RecipeManager.filterDisabled(enabledFeatures, this.ingredients));
   }
}
