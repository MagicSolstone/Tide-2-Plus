package net.minecraft.advancements;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.storage.loot.LootTable;

public class AdvancementRewards$Builder {
   private int experience;
   private final Builder<Holder<LootTable>> loot = ImmutableList.builder();
   private final Builder<ResourceKey<Recipe<?>>> recipes = ImmutableList.builder();
   private Optional<Identifier> function = Optional.empty();

   public static AdvancementRewards$Builder experience(final int amount) {
      return new AdvancementRewards$Builder().addExperience(amount);
   }

   public AdvancementRewards$Builder addExperience(final int amount) {
      this.experience += amount;
      return this;
   }

   public static AdvancementRewards$Builder loot(final Holder<LootTable> id) {
      return new AdvancementRewards$Builder().addLootTable(id);
   }

   public AdvancementRewards$Builder addLootTable(final Holder<LootTable> id) {
      this.loot.add(id);
      return this;
   }

   public static AdvancementRewards$Builder recipe(final ResourceKey<Recipe<?>> id) {
      return new AdvancementRewards$Builder().addRecipe(id);
   }

   public AdvancementRewards$Builder addRecipe(final ResourceKey<Recipe<?>> id) {
      this.recipes.add(id);
      return this;
   }

   public static AdvancementRewards$Builder function(final Identifier id) {
      return new AdvancementRewards$Builder().runs(id);
   }

   public AdvancementRewards$Builder runs(final Identifier function) {
      this.function = Optional.of(function);
      return this;
   }

   public AdvancementRewards build() {
      return new AdvancementRewards(this.experience, HolderSet.direct(this.loot.build()), this.recipes.build(), this.function.map(CacheableFunction::new));
   }
}
