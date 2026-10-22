package net.minecraft.client.gui.screens.friends;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum AddFriendWidget$State {
   EMPTY_INPUT,
   READY,
   SENDING,
   DISABLED;
}
