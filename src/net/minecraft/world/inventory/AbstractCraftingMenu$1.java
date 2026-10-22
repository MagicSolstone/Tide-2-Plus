package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.recipebook.ServerPlaceRecipe$CraftingMenuAccess;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

class AbstractCraftingMenu$1 implements ServerPlaceRecipe$CraftingMenuAccess<CraftingRecipe> {
   AbstractCraftingMenu$1(final AbstractCraftingMenu this$0) {
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
      this.this$0.resultSlots.clearContent();
      this.this$0.craftSlots.clearContent();
   }

   @Override
   public boolean recipeMatches(final RecipeHolder<CraftingRecipe> recipe) {
      return recipe.value().matches(this.this$0.craftSlots.asCraftInput(), this.this$0.owner().level());
   }
}
