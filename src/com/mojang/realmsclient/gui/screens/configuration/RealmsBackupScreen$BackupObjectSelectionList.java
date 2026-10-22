package com.mojang.realmsclient.gui.screens.configuration;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

@Environment(EnvType.CLIENT)
class RealmsBackupScreen$BackupObjectSelectionList extends ContainerObjectSelectionList<RealmsBackupScreen$Entry> {
   private static final int ITEM_HEIGHT = 36;

   public RealmsBackupScreen$BackupObjectSelectionList(final RealmsBackupScreen this$0) {
      Objects.requireNonNull(this$0);
      super(Minecraft.getInstance(), this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 36);
   }

   @Override
   public int getRowWidth() {
      return 300;
   }
}
