package net.minecraft.client.gui.screens.reporting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage$Player;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public interface ChatSelectionLogFiller$Output {
   void acceptMessage(int id, LoggedChatMessage$Player message);

   void acceptDivider(Component text);
}
