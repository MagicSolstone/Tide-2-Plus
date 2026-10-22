package com.mojang.realmsclient;

import com.mojang.realmsclient.dto.RealmsNotification;
import com.mojang.realmsclient.dto.RealmsNotification$VisitUrl;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServer$State;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class RealmsMainScreen$RealmSelectionList extends ObjectSelectionList<RealmsMainScreen$Entry> {
   public RealmsMainScreen$RealmSelectionList(final RealmsMainScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(Minecraft.getInstance(), this$0x.width, this$0x.height, 0, 36);
   }

   public void setSelected(final RealmsMainScreen$Entry selected) {
      super.setSelected(selected);
      this.this$0.updateButtonStates();
   }

   @Override
   public int getRowWidth() {
      return 300;
   }

   private void refreshEntries(final RealmsMainScreen realmsMainScreen) {
      RealmsMainScreen$Entry previouslySelected = this.getSelected();
      this.clearEntries();

      for (RealmsNotification notification : this.this$0.notifications) {
         if (notification instanceof RealmsNotification$VisitUrl visitUrl) {
            this.addEntriesForNotification(visitUrl, realmsMainScreen, previouslySelected);
            this.this$0.markNotificationsAsSeen(List.of(notification));
            break;
         }
      }

      this.refreshServerEntries(previouslySelected);
   }

   private void addEntriesForNotification(
      final RealmsNotification$VisitUrl visitUrl, final RealmsMainScreen realmsMainScreen, final RealmsMainScreen$Entry previouslySelected
   ) {
      Component message = visitUrl.getMessage();
      int messageHeight = RealmsMainScreen.access$300(this.this$0)
         .wordWrapHeight(message, RealmsMainScreen$NotificationMessageEntry.textWidth(this.getRowWidth()));
      RealmsMainScreen$NotificationMessageEntry entry = new RealmsMainScreen$NotificationMessageEntry(
         this.this$0, realmsMainScreen, messageHeight, message, visitUrl
      );
      this.addEntry(entry, 38 + messageHeight);
      if (previouslySelected instanceof RealmsMainScreen$NotificationMessageEntry notificationMessageEntry
         && notificationMessageEntry.getText().equals(message)) {
         this.setSelected((RealmsMainScreen$Entry)entry);
      }
   }

   private void refreshServerEntries(final RealmsMainScreen$Entry previouslySelected) {
      for (RealmsServer eligibleForSnapshotServer : this.this$0.availableSnapshotServers) {
         this.addEntry(new RealmsMainScreen$AvailableSnapshotEntry(this.this$0, eligibleForSnapshotServer));
      }

      for (RealmsServer server : this.this$0.serverList) {
         RealmsMainScreen$Entry entry;
         if (RealmsMainScreen.isSnapshot() && !server.isSnapshotRealm()) {
            if (server.state == RealmsServer$State.UNINITIALIZED) {
               continue;
            }

            entry = new RealmsMainScreen$ParentEntry(this.this$0, server);
         } else {
            entry = new RealmsMainScreen$ServerEntry(this.this$0, server);
         }

         this.addEntry(entry);
         if (previouslySelected instanceof RealmsMainScreen$ServerEntry serverEntry && serverEntry.serverData.id == server.id) {
            this.setSelected(entry);
         }
      }
   }
}
