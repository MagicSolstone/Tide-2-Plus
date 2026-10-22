package com.mojang.realmsclient.gui.screens;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsPdpScreen$PdpList extends ObjectSelectionList<RealmsPdpScreen$AbstractPdpEntry> {
   private RealmsPdpScreen$PdpList(final RealmsPdpScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(minecraft, this$0x.width, this$0x.layout.getContentHeight(), this$0x.layout.getHeaderHeight(), 45);
      if (this$0x.trialAvailable) {
         this.addHeader(
            RealmsPdpScreen.TRIAL_TITLE, new RealmsPdpScreen$PdpHeaderEntry$Subtitle(RealmsPdpScreen.TRIAL_DESCRIPTION, RealmsPdpScreen.TRIAL_AVAILABLE_SPRITE)
         );
      } else {
         this.addHeader(RealmsPdpScreen.TRIAL_TITLE, null);
      }

      this.addFeature(RealmsPdpScreen.FRIENDS_IMAGE, RealmsPdpScreen.FRIENDS_TITLE, RealmsPdpScreen.FRIENDS_DESCRIPTION);
      this.addFeature(RealmsPdpScreen.MINIGAMES_IMAGE, RealmsPdpScreen.MINIGAMES_TITLE, RealmsPdpScreen.MINIGAMES_DESCRIPTION);
      this.addFeature(
         RealmsPdpScreen.PRIVATE_SERVER_IMAGE,
         RealmsPdpScreen.PRIVATE_SERVER_TITLE,
         RealmsPdpScreen.PRIVATE_SERVER_BACKUPS,
         RealmsPdpScreen.PRIVATE_SERVER_EASY_TO_MANAGE,
         RealmsPdpScreen.PRIVATE_SERVER_WORLD_SLOTS,
         RealmsPdpScreen.PRIVATE_SERVER_SECURE
      );
   }

   private void addHeader(final Component title, @Nullable final RealmsPdpScreen$PdpHeaderEntry$Subtitle subTitle) {
      RealmsPdpScreen$PdpHeaderEntry entry = new RealmsPdpScreen$PdpHeaderEntry(this.this$0, title, subTitle);
      this.addEntry(entry, entry.entryHeight());
   }

   private void addFeature(final Identifier image, final Component title, final Component... descriptions) {
      RealmsPdpScreen$PdpFeatureEntry entry = new RealmsPdpScreen$PdpFeatureEntry(this.this$0, image, title, descriptions);
      this.addEntry(entry, entry.entryHeight());
   }

   @Override
   public int getRowWidth() {
      return 290;
   }
}
