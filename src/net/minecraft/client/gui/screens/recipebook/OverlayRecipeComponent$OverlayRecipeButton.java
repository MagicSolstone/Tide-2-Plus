package net.minecraft.client.gui.screens.recipebook;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

@Environment(EnvType.CLIENT)
abstract class OverlayRecipeComponent$OverlayRecipeButton extends AbstractWidget {
   private final RecipeDisplayId recipe;
   private final boolean isCraftable;
   private final List<OverlayRecipeComponent$OverlayRecipeButton$Pos> slots;

   public OverlayRecipeComponent$OverlayRecipeButton(
      final int this$0,
      final int x,
      final RecipeDisplayId y,
      final boolean recipe,
      final List<OverlayRecipeComponent$OverlayRecipeButton$Pos> isCraftable,
      final List slots
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(x, y, 24, 24, CommonComponents.EMPTY);
      this.slots = slots;
      this.recipe = recipe;
      this.isCraftable = isCraftable;
   }

   protected static OverlayRecipeComponent$OverlayRecipeButton$Pos createGridPos(final int gridXPos, final int gridYPos, final List<ItemStack> itemStacks) {
      return new OverlayRecipeComponent$OverlayRecipeButton$Pos(3 + gridXPos * 7, 3 + gridYPos * 7, itemStacks);
   }

   protected abstract Identifier getSprite(boolean isCraftable);

   @Override
   public void updateWidgetNarration(final NarrationElementOutput output) {
      this.defaultButtonNarrationText(output);
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getSprite(this.isCraftable), this.getX(), this.getY(), this.width, this.height);
      float gridPosX = this.getX() + 2;
      float gridPosY = this.getY() + 2;

      for (OverlayRecipeComponent$OverlayRecipeButton$Pos pos : this.slots) {
         graphics.pose().pushMatrix();
         graphics.pose().translate(gridPosX + pos.x, gridPosY + pos.y);
         graphics.pose().scale(0.375F, 0.375F);
         graphics.pose().translate(-8.0F, -8.0F);
         graphics.item(pos.selectIngredient(this.this$0.slotSelectTime.currentIndex()), 0, 0);
         graphics.pose().popMatrix();
      }
   }
}
