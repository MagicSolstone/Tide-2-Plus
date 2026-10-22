package net.minecraft.client.gui.screens.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData$Type;
import net.minecraft.client.server.LanServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ServerSelectionList$NetworkServerEntry extends ServerSelectionList$Entry {
   private static final int ICON_WIDTH = 32;
   private static final Component LAN_SERVER_HEADER = Component.translatable("lanServer.title");
   private static final Component HIDDEN_ADDRESS_TEXT = Component.translatable("selectServer.hiddenAddress");
   private final JoinMultiplayerScreen screen;
   protected final Minecraft minecraft;
   protected final LanServer serverData;

   protected ServerSelectionList$NetworkServerEntry(final JoinMultiplayerScreen screen, final LanServer serverData) {
      this.screen = screen;
      this.serverData = serverData;
      this.minecraft = Minecraft.getInstance();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.text(this.minecraft.font, LAN_SERVER_HEADER, this.getContentX() + 32 + 3, this.getContentY() + 1, -1);
      graphics.text(this.minecraft.font, this.serverData.getMotd(), this.getContentX() + 32 + 3, this.getContentY() + 12, -8355712);
      if (this.minecraft.options.hideServerAddress) {
         graphics.text(this.minecraft.font, HIDDEN_ADDRESS_TEXT, this.getContentX() + 32 + 3, this.getContentY() + 12 + 11, -8355712);
      } else {
         graphics.text(this.minecraft.font, this.serverData.getAddress(), this.getContentX() + 32 + 3, this.getContentY() + 12 + 11, -8355712);
      }
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      if (doubleClick) {
         this.join();
      }

      return super.mouseClicked(event, doubleClick);
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection()) {
         this.join();
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   @Override
   public void join() {
      this.screen.join(new ServerData(this.serverData.getMotd(), this.serverData.getAddress(), ServerData$Type.LAN));
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.getServerNarration());
   }

   public Component getServerNarration() {
      return Component.empty().append(LAN_SERVER_HEADER).append(CommonComponents.SPACE).append(this.serverData.getMotd());
   }

   @Override
   protected boolean matches(final ServerSelectionList$Entry other) {
      return other instanceof ServerSelectionList$NetworkServerEntry networkServerEntry && networkServerEntry.serverData == this.serverData;
   }
}
