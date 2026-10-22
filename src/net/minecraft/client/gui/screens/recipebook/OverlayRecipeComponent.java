package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class OverlayRecipeComponent implements GuiEventListener, Renderable {
   private static final Identifier OVERLAY_RECIPE_SPRITE = Identifier.withDefaultNamespace("recipe_book/overlay_recipe");
   private static final int MAX_ROW = 4;
   private static final int MAX_ROW_LARGE = 5;
   private static final float ITEM_RENDER_SCALE = 0.375F;
   public static final int BUTTON_SIZE = 25;
   private final List<OverlayRecipeComponent$OverlayRecipeButton> recipeButtons = Lists.newArrayList();
   private boolean isVisible;
   private int x;
   private int y;
   private RecipeCollection collection = RecipeCollection.EMPTY;
   @Nullable
   private RecipeDisplayId lastRecipeClicked;
   private final SlotSelectTime slotSelectTime;
   private final boolean isFurnaceMenu;

   public OverlayRecipeComponent(final SlotSelectTime slotSelectTime, final boolean isFurnaceMenu) {
      this.slotSelectTime = slotSelectTime;
      this.isFurnaceMenu = isFurnaceMenu;
   }

   public void init(
      final RecipeCollection collection,
      final ContextMap context,
      final boolean isFiltering,
      final int buttonX,
      final int buttonY,
      final int centerX,
      final int centerY,
      final float buttonWidth
   ) {
      this.collection = collection;
      List<RecipeDisplayEntry> craftable = collection.getSelectedRecipes(RecipeCollection$CraftableStatus.CRAFTABLE);
      List<RecipeDisplayEntry> unCraftable = isFiltering
         ? Collections.emptyList()
         : collection.getSelectedRecipes(RecipeCollection$CraftableStatus.NOT_CRAFTABLE);
      int craftables = craftable.size();
      int total = craftables + unCraftable.size();
      int maxRow = total <= 16 ? 4 : 5;
      int rows = (int)Math.ceil((float)total / maxRow);
      this.x = buttonX;
      this.y = buttonY;
      float rightPos = this.x + Math.min(total, maxRow) * 25;
      float maxLeftPos = centerX + 50;
      if (rightPos > maxLeftPos) {
         this.x = (int)(this.x - buttonWidth * (int)((rightPos - maxLeftPos) / buttonWidth));
      }

      float bottomPos = this.y + rows * 25;
      float maxBottomPos = centerY + 50;
      if (bottomPos > maxBottomPos) {
         this.y = (int)(this.y - buttonWidth * Mth.ceil((bottomPos - maxBottomPos) / buttonWidth));
      }

      float topPos = this.y;
      float maxTopPos = centerY - 100;
      if (topPos < maxTopPos) {
         this.y = (int)(this.y - buttonWidth * Mth.ceil((topPos - maxTopPos) / buttonWidth));
      }

      this.isVisible = true;
      this.recipeButtons.clear();

      for (int i = 0; i < total; i++) {
         boolean canCraft = i < craftables;
         RecipeDisplayEntry recipe = canCraft ? craftable.get(i) : unCraftable.get(i - craftables);
         int x = this.x + 4 + 25 * (i % maxRow);
         int y = this.y + 5 + 25 * (i / maxRow);
         if (this.isFurnaceMenu) {
            this.recipeButtons.add(new OverlayRecipeComponent$OverlaySmeltingRecipeButton(this, x, y, recipe.id(), recipe.display(), context, canCraft));
         } else {
            this.recipeButtons.add(new OverlayRecipeComponent$OverlayCraftingRecipeButton(this, x, y, recipe.id(), recipe.display(), context, canCraft));
         }
      }

      this.lastRecipeClicked = null;
   }

   public RecipeCollection getRecipeCollection() {
      return this.collection;
   }

   @Nullable
   public RecipeDisplayId getLastRecipeClicked() {
      return this.lastRecipeClicked;
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      if (event.button() != 1) {
         return false;
      } else {
         for (OverlayRecipeComponent$OverlayRecipeButton recipeButton : this.recipeButtons) {
            if (recipeButton.mouseClicked(event, doubleClick)) {
               this.lastRecipeClicked = recipeButton.recipe;
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean isMouseOver(final double mouseX, final double mouseY) {
      return false;
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      if (this.isVisible) {
         int maxRow = this.recipeButtons.size() <= 16 ? 4 : 5;
         int width = Math.min(this.recipeButtons.size(), maxRow);
         int height = Mth.ceil((float)this.recipeButtons.size() / maxRow);
         int border = 4;
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, OVERLAY_RECIPE_SPRITE, this.x, this.y, width * 25 + 8, height * 25 + 8);

         for (OverlayRecipeComponent$OverlayRecipeButton component : this.recipeButtons) {
            component.extractRenderState(graphics, mouseX, mouseY, a);
         }
      }
   }

   public void setVisible(final boolean visible) {
      this.isVisible = visible;
   }

   public boolean isVisible() {
      return this.isVisible;
   }

   @Override
   public void setFocused(final boolean focused) {
   }

   @Override
   public boolean isFocused() {
      return false;
   }
}
