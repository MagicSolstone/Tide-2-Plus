package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeCraftedTrigger extends SimpleCriterionTrigger<RecipeCraftedTrigger$TriggerInstance> {
   @Override
   public Codec<RecipeCraftedTrigger$TriggerInstance> codec() {
      return RecipeCraftedTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ResourceKey<Recipe<?>> id, final List<ItemStack> usedIngredients) {
      this.trigger(player, t -> t.matches(id, usedIngredients));
   }
}
