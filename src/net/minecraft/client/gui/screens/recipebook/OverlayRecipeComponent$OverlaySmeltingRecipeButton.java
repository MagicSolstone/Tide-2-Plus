package net.minecraft.client.gui.screens.recipebook;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

@Environment(EnvType.CLIENT)
class OverlayRecipeComponent$OverlaySmeltingRecipeButton extends OverlayRecipeComponent$OverlayRecipeButton {
   private static final Identifier ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay");
   private static final Identifier HIGHLIGHTED_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_highlighted");
   private static final Identifier DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled");
   private static final Identifier HIGHLIGHTED_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/furnace_overlay_disabled_highlighted");

   public OverlayRecipeComponent$OverlaySmeltingRecipeButton(
      final OverlayRecipeComponent this$0,
      final int x,
      final int y,
      final RecipeDisplayId id,
      final RecipeDisplay recipe,
      final ContextMap context,
      final boolean isCraftable
   ) {
      Objects.requireNonNull(this$0);
      super((int)this$0, x, y, (boolean)id, isCraftable, calculateIngredientsPositions(recipe, context));
   }

   private static List<OverlayRecipeComponent$OverlayRecipeButton$Pos> calculateIngredientsPositions(final RecipeDisplay recipe, final ContextMap context) {
      if (recipe instanceof FurnaceRecipeDisplay furnaceRecipe) {
         List<ItemStack> items = furnaceRecipe.ingredient().resolveForStacks(context);
         if (!items.isEmpty()) {
            return List.of(createGridPos(1, 1, items));
         }
      }

      return List.of();
   }

   @Override
   protected Identifier getSprite(final boolean isCraftable) {
      if (isCraftable) {
         return this.isHoveredOrFocused() ? HIGHLIGHTED_ENABLED_SPRITE : ENABLED_SPRITE;
      } else {
         return this.isHoveredOrFocused() ? HIGHLIGHTED_DISABLED_SPRITE : DISABLED_SPRITE;
      }
   }
}
