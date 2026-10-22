package net.minecraft.world.item.crafting;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class RecipeManager implements RecipeAccess {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<ResourceKey<RecipePropertySet>, RecipeManager$IngredientExtractor> RECIPE_PROPERTY_SETS = Map.of(
      RecipePropertySet.SMITHING_ADDITION,
      recipe -> recipe instanceof SmithingRecipe smithingRecipe ? smithingRecipe.additionIngredient() : Optional.empty(),
      RecipePropertySet.SMITHING_BASE,
      recipe -> recipe instanceof SmithingRecipe smithingRecipe ? Optional.of(smithingRecipe.baseIngredient()) : Optional.empty(),
      RecipePropertySet.SMITHING_TEMPLATE,
      recipe -> recipe instanceof SmithingRecipe smithingRecipe ? smithingRecipe.templateIngredient() : Optional.empty(),
      RecipePropertySet.FURNACE_INPUT,
      forSingleInput(RecipeType.SMELTING),
      RecipePropertySet.BLAST_FURNACE_INPUT,
      forSingleInput(RecipeType.BLASTING),
      RecipePropertySet.SMOKER_INPUT,
      forSingleInput(RecipeType.SMOKING),
      RecipePropertySet.CAMPFIRE_INPUT,
      forSingleInput(RecipeType.CAMPFIRE_COOKING),
      RecipePropertySet.BREWING_INPUTS,
      recipe -> recipe instanceof BrewingRecipe brewingRecipe ? Optional.of(brewingRecipe.getInput().ingredient()) : Optional.empty(),
      RecipePropertySet.BREWING_REAGENTS,
      recipe -> recipe instanceof BrewingRecipe brewingRecipe ? Optional.of(brewingRecipe.getReagent().ingredient()) : Optional.empty()
   );
   private final RecipeMap recipes;
   private Map<ResourceKey<RecipePropertySet>, RecipePropertySet> propertySets = Map.of();
   private SelectableRecipe$SingleInputSet<StonecutterRecipe> stonecutterRecipes = SelectableRecipe$SingleInputSet.empty();
   private List<RecipeManager$ServerDisplayInfo> allDisplays = List.of();
   private Map<ResourceKey<Recipe<?>>, List<RecipeManager$ServerDisplayInfo>> recipeToDisplay = Map.of();
   private final Collection<RecipeHolder<?>> learnableRecipes;

   public RecipeManager(final HolderLookup$Provider registries) {
      HolderLookup$RegistryLookup<Recipe<?>> recipeRegistries = registries.lookupOrThrow(Registries.RECIPE);
      this.recipes = RecipeMap.create(recipeRegistries);
      this.learnableRecipes = this.recipes.values().stream().filter(r -> !r.value().isSpecial()).collect(Collectors.toUnmodifiableList());
   }

   public void finalizeRecipeLoading(final FeatureFlagSet enabledFlags) {
      List<SelectableRecipe$SingleInputEntry<StonecutterRecipe>> stonecutterRecipes = new ArrayList<>();
      List<RecipeManager$IngredientCollector> propertySetCollectors = RECIPE_PROPERTY_SETS.entrySet()
         .stream()
         .map(e -> new RecipeManager$IngredientCollector(e.getKey(), e.getValue()))
         .toList();
      this.recipes
         .values()
         .forEach(
            recipeHolder -> {
               Recipe<?> recipe = recipeHolder.value();
               if (!recipe.isSpecial() && recipe.placementInfo().isImpossibleToPlace()) {
                  LOGGER.warn("Recipe {} can't be placed due to empty ingredients and will be ignored", recipeHolder.id().identifier());
               } else {
                  propertySetCollectors.forEach(c -> c.accept(recipe));
                  if (recipe instanceof StonecutterRecipe stonecutterRecipe
                     && isIngredientEnabled(enabledFlags, stonecutterRecipe.input())
                     && stonecutterRecipe.resultDisplay().isEnabled(enabledFlags)) {
                     stonecutterRecipes.add(
                        new SelectableRecipe$SingleInputEntry<>(
                           stonecutterRecipe.input(),
                           new SelectableRecipe<>(stonecutterRecipe.resultDisplay(), Optional.of((RecipeHolder<StonecutterRecipe>)recipeHolder))
                        )
                     );
                  }
               }
            }
         );
      this.propertySets = propertySetCollectors.stream().collect(Collectors.toUnmodifiableMap(c -> c.key, c -> c.asPropertySet(enabledFlags)));
      this.stonecutterRecipes = new SelectableRecipe$SingleInputSet<>(stonecutterRecipes);
      this.allDisplays = unpackRecipeInfo(this.recipes.values(), enabledFlags);
      this.recipeToDisplay = this.allDisplays.stream().collect(Collectors.groupingBy(r -> r.parent.id(), IdentityHashMap::new, Collectors.toList()));
   }

   private static List<Ingredient> filterDisabled(final FeatureFlagSet enabledFlags, final List<Ingredient> ingredients) {
      ingredients.removeIf(e -> !isIngredientEnabled(enabledFlags, e));
      return ingredients;
   }

   private static boolean isIngredientEnabled(final FeatureFlagSet enabledFlags, final Ingredient ingredient) {
      return ingredient.items().allMatch(i -> i.value().isEnabled(enabledFlags));
   }

   public <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(
      final RecipeType<T> type, final I input, final Level level, @Nullable final ResourceKey<Recipe<?>> recipeHint
   ) {
      RecipeHolder<T> hintedRecipe = recipeHint != null ? this.byKeyTyped(type, recipeHint) : null;
      return this.getRecipeFor(type, input, level, hintedRecipe);
   }

   public <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(
      final RecipeType<T> type, final I input, final Level level, @Nullable final RecipeHolder<T> recipeHint
   ) {
      return recipeHint != null && recipeHint.value().matches(input, level) ? Optional.of(recipeHint) : this.getRecipeFor(type, input, level);
   }

   public <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(final RecipeType<T> type, final I input, final Level level) {
      return this.recipes.getRecipesFor(type, input, level).findFirst();
   }

   public Optional<RecipeHolder<?>> byKey(final ResourceKey<Recipe<?>> recipeId) {
      return Optional.ofNullable(this.recipes.byKey(recipeId));
   }

   @Nullable
   private <T extends Recipe<?>> RecipeHolder<T> byKeyTyped(final RecipeType<T> type, final ResourceKey<Recipe<?>> recipeId) {
      RecipeHolder<?> recipe = this.recipes.byKey(recipeId);
      return (RecipeHolder<T>)(recipe != null && recipe.value().getType().equals(type) ? recipe : null);
   }

   public Map<ResourceKey<RecipePropertySet>, RecipePropertySet> getSynchronizedItemProperties() {
      return this.propertySets;
   }

   public SelectableRecipe$SingleInputSet<StonecutterRecipe> getSynchronizedStonecutterRecipes() {
      return this.stonecutterRecipes;
   }

   @Override
   public RecipePropertySet propertySet(final ResourceKey<RecipePropertySet> id) {
      return this.propertySets.getOrDefault(id, RecipePropertySet.EMPTY);
   }

   @Override
   public SelectableRecipe$SingleInputSet<StonecutterRecipe> stonecutterRecipes() {
      return this.stonecutterRecipes;
   }

   public Collection<RecipeHolder<?>> getRecipes() {
      return this.recipes.values();
   }

   public Collection<RecipeHolder<?>> getLearnableRecipes() {
      return this.learnableRecipes;
   }

   @Nullable
   public RecipeManager$ServerDisplayInfo getRecipeFromDisplay(final RecipeDisplayId id) {
      int index = id.index();
      return index >= 0 && index < this.allDisplays.size() ? this.allDisplays.get(index) : null;
   }

   public void listDisplaysForRecipe(final ResourceKey<Recipe<?>> id, final Consumer<RecipeDisplayEntry> output) {
      List<RecipeManager$ServerDisplayInfo> recipes = this.recipeToDisplay.get(id);
      if (recipes != null) {
         recipes.forEach(e -> output.accept(e.display));
      }
   }

   public static <I extends RecipeInput, T extends Recipe<I>> RecipeManager$CachedCheck<I, T> createCheck(final RecipeType<T> type) {
      return new RecipeManager$1(type);
   }

   private static List<RecipeManager$ServerDisplayInfo> unpackRecipeInfo(final Iterable<RecipeHolder<?>> recipes, final FeatureFlagSet enabledFeatures) {
      List<RecipeManager$ServerDisplayInfo> result = new ArrayList<>();
      Object2IntMap<String> recipeGroups = new Object2IntOpenHashMap();

      for (RecipeHolder<?> recipeHolder : recipes) {
         Recipe<?> recipe = recipeHolder.value();
         OptionalInt groupId;
         if (recipe.group().isEmpty()) {
            groupId = OptionalInt.empty();
         } else {
            groupId = OptionalInt.of(recipeGroups.computeIfAbsent(recipe.group(), idx -> recipeGroups.size()));
         }

         Optional<List<Ingredient>> placementCheck;
         if (recipe.isSpecial()) {
            placementCheck = Optional.empty();
         } else {
            placementCheck = Optional.of(recipe.placementInfo().ingredients());
         }

         for (RecipeDisplay recipeDisplay : recipe.display()) {
            if (recipeDisplay.isEnabled(enabledFeatures)) {
               int nextDisplayId = result.size();
               RecipeDisplayId id = new RecipeDisplayId(nextDisplayId);
               RecipeDisplayEntry entry = new RecipeDisplayEntry(id, recipeDisplay, groupId, recipe.recipeBookCategory(), placementCheck);
               result.add(new RecipeManager$ServerDisplayInfo(entry, recipeHolder));
            }
         }
      }

      return result;
   }

   private static RecipeManager$IngredientExtractor forSingleInput(final RecipeType<? extends SingleItemRecipe> type) {
      return recipe -> recipe.getType() == type && recipe instanceof SingleItemRecipe singleItemRecipe
         ? Optional.of(singleItemRecipe.input())
         : Optional.empty();
   }
}
