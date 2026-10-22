package com.mojang.realmsclient.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record RealmsPdpScreen$PdpHeaderEntry$Subtitle(Component subTitle, Identifier icon) {
}
