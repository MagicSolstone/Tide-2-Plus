package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RecipeUnlockedTrigger extends SimpleCriterionTrigger<RecipeUnlockedTrigger$TriggerInstance> {
   @Override
   public Codec<RecipeUnlockedTrigger$TriggerInstance> codec() {
      return RecipeUnlockedTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final RecipeHolder<?> recipe) {
      this.trigger(player, t -> t.matches(recipe));
   }

   public static Criterion<RecipeUnlockedTrigger$TriggerInstance> unlocked(final Holder<Recipe<?>> recipe) {
      return unlocked(HolderSet.direct(recipe));
   }

   public static Criterion<RecipeUnlockedTrigger$TriggerInstance> unlocked(final HolderSet<Recipe<?>> recipe) {
      return CriteriaTriggers.RECIPE_UNLOCKED.createCriterion(new RecipeUnlockedTrigger$TriggerInstance(Optional.empty(), recipe));
   }
}
