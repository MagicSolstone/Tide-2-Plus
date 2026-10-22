package com.mojang.realmsclient.gui.screens;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

@Environment(EnvType.CLIENT)
class RealmsPendingInvitesScreen$PendingInvitationSelectionList extends ContainerObjectSelectionList<RealmsPendingInvitesScreen$Entry> {
   public static final int ITEM_HEIGHT = 36;

   public RealmsPendingInvitesScreen$PendingInvitationSelectionList(final RealmsPendingInvitesScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0);
      super(minecraft, this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 36);
   }

   @Override
   public int getRowWidth() {
      return 280;
   }

   public boolean hasPendingInvites() {
      return this.getItemCount() == 0;
   }

   public void removeInvitation(final RealmsPendingInvitesScreen$Entry entry) {
      this.removeEntry(entry);
   }
}
