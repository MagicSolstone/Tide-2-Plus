package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RealmsServer;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

@Environment(EnvType.CLIENT)
class RealmsPlayersTab$InvitedObjectSelectionList extends ContainerObjectSelectionList<RealmsPlayersTab$Entry> {
   private static final int PLAYER_ENTRY_HEIGHT = 36;

   public RealmsPlayersTab$InvitedObjectSelectionList(final RealmsPlayersTab this$0, final int width, final int height) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(Minecraft.getInstance(), width, height, this$0x.configurationScreen.getHeaderHeight(), 36);
   }

   private void updateList(final RealmsServer serverData) {
      this.clearEntries();
      this.populateList(serverData);
   }

   private void populateList(final RealmsServer serverData) {
      RealmsPlayersTab$HeaderEntry entry = new RealmsPlayersTab$HeaderEntry(this.this$0);
      this.addEntry(entry, entry.height(9));

      for (RealmsPlayersTab$PlayerEntry newChild : serverData.players.stream().map(x$0 -> new RealmsPlayersTab$PlayerEntry(this.this$0, x$0)).toList()) {
         this.addEntry(newChild);
      }
   }

   @Override
   protected void extractListBackground(final GuiGraphicsExtractor graphics) {
   }

   @Override
   protected void extractListSeparators(final GuiGraphicsExtractor graphics) {
   }

   @Override
   public int getRowWidth() {
      return 300;
   }
}
