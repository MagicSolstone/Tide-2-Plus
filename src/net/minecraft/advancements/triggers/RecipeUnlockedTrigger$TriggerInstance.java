package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record RecipeUnlockedTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, HolderSet<Recipe<?>> recipes)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<RecipeUnlockedTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(RecipeUnlockedTrigger$TriggerInstance::player),
            Recipe.LIST_CODEC.fieldOf("recipes").forGetter(RecipeUnlockedTrigger$TriggerInstance::recipes)
         )
         .apply(i, RecipeUnlockedTrigger$TriggerInstance::new)
   );

   public boolean matches(final RecipeHolder<?> recipe) {
      return this.recipes.stream().anyMatch(holder -> holder.is(recipe.id()));
   }
}
