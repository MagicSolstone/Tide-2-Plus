package net.minecraft.data.recipes;

import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.advancements.Advancement$Builder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

class RecipeProvider$1 implements RecipeOutput {
   RecipeProvider$1(final RecipeProvider this$0, final BootstrapContext val$recipeOutput, final BootstrapContext val$advancementOutput) {
      this.val$recipeOutput = val$recipeOutputx;
      this.val$advancementOutput = val$advancementOutputx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void accept(final ResourceKey<Recipe<?>> id, final Recipe<?> recipe, @Nullable final AdvancementHolder advancementHolder) {
      this.val$recipeOutput.register(id, recipe);
      if (advancementHolder != null) {
         this.acceptAdvancement(advancementHolder);
      }
   }

   private void acceptAdvancement(final AdvancementHolder advancementHolder) {
      advancementHolder.register(this.val$advancementOutput);
   }

   @Override
   public Advancement$Builder advancement() {
      return Advancement$Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
   }

   @Override
   public <S> HolderGetter<S> lookup(final ResourceKey<? extends Registry<? extends S>> key) {
      return this.val$recipeOutput.lookup(key);
   }

   @Deprecated
   @Override
   public <S> Stream<Holder$Reference<S>> listContextElements(final ResourceKey<? extends Registry<? extends S>> key) {
      return this.val$recipeOutput.listContextElements(key);
   }
}
