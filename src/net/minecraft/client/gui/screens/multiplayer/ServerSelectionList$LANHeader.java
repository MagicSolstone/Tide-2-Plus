package net.minecraft.client.gui.screens.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LoadingDotsWidget;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ServerSelectionList$LANHeader extends ServerSelectionList$Entry {
   private final Minecraft minecraft = Minecraft.getInstance();
   private final LoadingDotsWidget loadingDotsWidget = new LoadingDotsWidget(this.minecraft.font, ServerSelectionList.SCANNING_LABEL);

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.loadingDotsWidget.setPosition(this.getContentXMiddle() - this.minecraft.font.width(ServerSelectionList.SCANNING_LABEL) / 2, this.getContentY());
      this.loadingDotsWidget.extractRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public Component getNarration() {
      return ServerSelectionList.SCANNING_LABEL;
   }

   @Override
   protected boolean matches(final ServerSelectionList$Entry other) {
      return other instanceof ServerSelectionList$LANHeader;
   }

   @Override
   public void join() {
   }
}
