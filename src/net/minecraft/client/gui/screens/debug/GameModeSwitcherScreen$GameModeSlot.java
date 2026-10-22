package net.minecraft.client.gui.screens.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;

@Environment(EnvType.CLIENT)
public class GameModeSwitcherScreen$GameModeSlot extends AbstractWidget {
   private final GameModeSwitcherScreen$GameModeIcon icon;
   private boolean isSelected;

   public GameModeSwitcherScreen$GameModeSlot(final GameModeSwitcherScreen$GameModeIcon icon, final int x, final int y) {
      super(x, y, 26, 26, icon.name);
      this.icon = icon;
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      this.extractSlot(graphics);
      if (this.isSelected) {
         this.extractSelection(graphics);
      }

      this.icon.extractIcon(graphics, this.getX() + 5, this.getY() + 5);
   }

   @Override
   public void updateWidgetNarration(final NarrationElementOutput output) {
      this.defaultButtonNarrationText(output);
   }

   @Override
   public boolean isHoveredOrFocused() {
      return super.isHoveredOrFocused() || this.isSelected;
   }

   public void setSelected(final boolean isSelected) {
      this.isSelected = isSelected;
   }

   private void extractSlot(final GuiGraphicsExtractor graphics) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, GameModeSwitcherScreen.SLOT_SPRITE, this.getX(), this.getY(), 26, 26);
   }

   private void extractSelection(final GuiGraphicsExtractor graphics) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, GameModeSwitcherScreen.SELECTION_SPRITE, this.getX(), this.getY(), 26, 26);
   }
}
