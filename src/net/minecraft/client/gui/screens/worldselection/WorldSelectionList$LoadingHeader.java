package net.minecraft.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingDotsText;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public class WorldSelectionList$LoadingHeader extends WorldSelectionList$Entry {
   private static final Component LOADING_LABEL = Component.translatable("selectWorld.loading_list");
   private final Minecraft minecraft;

   public WorldSelectionList$LoadingHeader(final Minecraft minecraft) {
      this.minecraft = minecraft;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int labelX = (this.minecraft.gui.screen().width - this.minecraft.font.width(LOADING_LABEL)) / 2;
      int labelY = this.getContentY() + (this.getContentHeight() - 9) / 2;
      graphics.text(this.minecraft.font, LOADING_LABEL, labelX, labelY, -1);
      String dots = LoadingDotsText.get(Util.getMillis());
      int dotsX = (this.minecraft.gui.screen().width - this.minecraft.font.width(dots)) / 2;
      int dotsY = labelY + 9;
      graphics.text(this.minecraft.font, dots, dotsX, dotsY, -8355712);
   }

   @Override
   public Component getNarration() {
      return LOADING_LABEL;
   }
}
