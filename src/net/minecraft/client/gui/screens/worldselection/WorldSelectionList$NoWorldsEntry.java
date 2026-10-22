package net.minecraft.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class WorldSelectionList$NoWorldsEntry extends WorldSelectionList$Entry {
   private final StringWidget stringWidget;

   public WorldSelectionList$NoWorldsEntry(final Component component, final Font font) {
      this.stringWidget = new StringWidget(component, font);
   }

   @Override
   public Component getNarration() {
      return this.stringWidget.getMessage();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.stringWidget.setPosition(this.getContentXMiddle() - this.stringWidget.getWidth() / 2, this.getContentYMiddle() - this.stringWidget.getHeight() / 2);
      this.stringWidget.extractRenderState(graphics, mouseX, mouseY, a);
   }
}
