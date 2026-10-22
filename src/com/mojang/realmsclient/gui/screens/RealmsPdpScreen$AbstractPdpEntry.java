package com.mojang.realmsclient.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
abstract class RealmsPdpScreen$AbstractPdpEntry extends ObjectSelectionList$Entry<RealmsPdpScreen$AbstractPdpEntry> {
   protected final Component title;

   public RealmsPdpScreen$AbstractPdpEntry(final Component title) {
      this.title = title;
   }
}
