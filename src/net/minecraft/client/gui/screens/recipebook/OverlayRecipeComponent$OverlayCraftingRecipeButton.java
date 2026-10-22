package net.minecraft.client.gui.screens.recipebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

@Environment(EnvType.CLIENT)
class OverlayRecipeComponent$OverlayCraftingRecipeButton extends OverlayRecipeComponent$OverlayRecipeButton {
   private static final Identifier ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay");
   private static final Identifier HIGHLIGHTED_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_highlighted");
   private static final Identifier DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled");
   private static final Identifier HIGHLIGHTED_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/crafting_overlay_disabled_highlighted");
   private static final int GRID_WIDTH = 3;
   private static final int GRID_HEIGHT = 3;

   public OverlayRecipeComponent$OverlayCraftingRecipeButton(
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
      List<OverlayRecipeComponent$OverlayRecipeButton$Pos> result = new ArrayList<>();
      switch (recipe) {
         case ShapedCraftingRecipeDisplay shaped:
            PlaceRecipeHelper.placeRecipe(3, 3, shaped.width(), shaped.height(), shaped.ingredients(), (ingredient, gridIndex, gridXPos, gridYPos) -> {
               List<ItemStack> itemsx = ingredient.resolveForStacks(context);
               if (!itemsx.isEmpty()) {
                  result.add(createGridPos(gridXPos, gridYPos, itemsx));
               }
            });
            break;
         case ShapelessCraftingRecipeDisplay shapeless:
            label19: {
               List<SlotDisplay> ingredients = shapeless.ingredients();

               for (int i = 0; i < ingredients.size(); i++) {
                  List<ItemStack> items = ingredients.get(i).resolveForStacks(context);
                  if (!items.isEmpty()) {
                     result.add(createGridPos(i % 3, i / 3, items));
                  }
               }
               break label19;
            }
         default:
      }

      return result;
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
