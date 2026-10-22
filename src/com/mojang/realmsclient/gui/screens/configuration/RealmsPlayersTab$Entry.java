package com.mojang.realmsclient.gui.screens.configuration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ContainerObjectSelectionList$Entry;

@Environment(EnvType.CLIENT)
abstract class RealmsPlayersTab$Entry extends ContainerObjectSelectionList$Entry<RealmsPlayersTab$Entry> {
   private RealmsPlayersTab$Entry() {
   }
}
