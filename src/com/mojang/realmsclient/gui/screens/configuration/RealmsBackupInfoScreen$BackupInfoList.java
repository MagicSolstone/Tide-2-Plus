package com.mojang.realmsclient.gui.screens.configuration;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;

@Environment(EnvType.CLIENT)
class RealmsBackupInfoScreen$BackupInfoList extends ObjectSelectionList<RealmsBackupInfoScreen$BackupInfoListEntry> {
   public RealmsBackupInfoScreen$BackupInfoList(final RealmsBackupInfoScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0);
      super(minecraft, this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 36);
      if (this$0.backup.changeList != null) {
         this$0.backup.changeList.forEach((key, value) -> this.addEntry(new RealmsBackupInfoScreen$BackupInfoListEntry(this$0, key, value)));
      }
   }
}
