package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
abstract class BeaconScreen$BeaconScreenButton extends AbstractButton implements BeaconScreen$BeaconButton {
   private boolean selected;

   protected BeaconScreen$BeaconScreenButton(final int x, final int y) {
      super(x, y, 22, 22, CommonComponents.EMPTY);
   }

   protected BeaconScreen$BeaconScreenButton(final int x, final int y, final Component component) {
      super(x, y, 22, 22, component);
   }

   @Override
   public void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      Identifier sprite;
      if (!this.active) {
         sprite = BeaconScreen.BUTTON_DISABLED_SPRITE;
      } else if (this.selected) {
         sprite = BeaconScreen.BUTTON_SELECTED_SPRITE;
      } else if (this.isHoveredOrFocused()) {
         sprite = BeaconScreen.BUTTON_HIGHLIGHTED_SPRITE;
      } else {
         sprite = BeaconScreen.BUTTON_SPRITE;
      }

      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX(), this.getY(), this.width, this.height);
      this.extractIcon(graphics);
   }

   protected abstract void extractIcon(final GuiGraphicsExtractor graphics);

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(final boolean selected) {
      this.selected = selected;
   }

   @Override
   public void updateWidgetNarration(final NarrationElementOutput output) {
      this.defaultButtonNarrationText(output);
   }
}
