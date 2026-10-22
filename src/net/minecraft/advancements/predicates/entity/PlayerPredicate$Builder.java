package net.minecraft.advancements.predicates.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.advancements.predicates.FoodPredicate;
import net.minecraft.advancements.predicates.GameTypePredicate;
import net.minecraft.advancements.predicates.InputPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.core.Holder$Reference;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.crafting.Recipe;

public class PlayerPredicate$Builder {
   private MinMaxBounds$Ints level = MinMaxBounds$Ints.ANY;
   private FoodPredicate food = FoodPredicate.ANY;
   private GameTypePredicate gameType = GameTypePredicate.ANY;
   private final Builder<PlayerPredicate$StatMatcher<?>> stats = ImmutableList.builder();
   private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes = new Object2BooleanOpenHashMap();
   private final Map<Identifier, PlayerPredicate$AdvancementPredicate> advancements = Maps.newHashMap();
   private Optional<EntityPredicate> lookingAt = Optional.empty();
   private Optional<InputPredicate> input = Optional.empty();

   public static PlayerPredicate$Builder player() {
      return new PlayerPredicate$Builder();
   }

   public PlayerPredicate$Builder setLevel(final MinMaxBounds$Ints level) {
      this.level = level;
      return this;
   }

   public PlayerPredicate$Builder setFood(final FoodPredicate food) {
      this.food = food;
      return this;
   }

   public <T> PlayerPredicate$Builder addStat(final StatType<T> type, final Holder$Reference<T> value, final MinMaxBounds$Ints range) {
      this.stats.add(new PlayerPredicate$StatMatcher<>(type, value, range));
      return this;
   }

   public PlayerPredicate$Builder addRecipe(final ResourceKey<Recipe<?>> recipe, final boolean present) {
      this.recipes.put(recipe, present);
      return this;
   }

   public PlayerPredicate$Builder setGameType(final GameTypePredicate gameType) {
      this.gameType = gameType;
      return this;
   }

   public PlayerPredicate$Builder setLookingAt(final EntityPredicate$Builder lookingAt) {
      this.lookingAt = Optional.of(lookingAt.build());
      return this;
   }

   public PlayerPredicate$Builder checkAdvancementDone(final Identifier advancement, final boolean isDone) {
      this.advancements.put(advancement, new PlayerPredicate$AdvancementDonePredicate(isDone));
      return this;
   }

   public PlayerPredicate$Builder checkAdvancementCriterions(final Identifier advancement, final Map<String, Boolean> criterions) {
      this.advancements.put(advancement, new PlayerPredicate$AdvancementCriterionsPredicate(new Object2BooleanOpenHashMap(criterions)));
      return this;
   }

   public PlayerPredicate$Builder hasInput(final InputPredicate input) {
      this.input = Optional.of(input);
      return this;
   }

   public PlayerPredicate build() {
      return new PlayerPredicate(this.level, this.food, this.gameType, this.stats.build(), this.recipes, this.advancements, this.lookingAt, this.input);
   }
}
