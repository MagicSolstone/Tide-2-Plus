package net.minecraft.client.multiplayer.chat.report;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage$Player;

@Environment(EnvType.CLIENT)
public interface ChatReportContextBuilder$Handler {
   void accept(int id, LoggedChatMessage$Player event);
}
