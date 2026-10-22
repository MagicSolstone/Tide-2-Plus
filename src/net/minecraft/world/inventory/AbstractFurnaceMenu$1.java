package net.minecraft.world.inventory;

import java.util.List;
import java.util.Objects;
import net.minecraft.recipebook.ServerPlaceRecipe$CraftingMenuAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;

class AbstractFurnaceMenu$1 implements ServerPlaceRecipe$CraftingMenuAccess<AbstractCookingRecipe> {
   AbstractFurnaceMenu$1(final AbstractFurnaceMenu this$0, final List val$slotsToClear, final ServerLevel val$level) {
      this.val$slotsToClear = val$slotsToClearx;
      this.val$level = val$levelx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void fillCraftSlotsStackedContents(final StackedItemContents stackedContents) {
      this.this$0.fillCraftSlotsStackedContents(stackedContents);
   }

   @Override
   public void clearCraftingContent() {
      this.val$slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
   }

   @Override
   public boolean recipeMatches(final RecipeHolder<AbstractCookingRecipe> recipe) {
      return recipe.value().matches(new SingleRecipeInput(this.this$0.container.getItem(0)), this.val$level);
   }
}
