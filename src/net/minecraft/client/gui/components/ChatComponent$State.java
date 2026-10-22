package net.minecraft.client.gui.components;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.GuiMessage;

@Environment(EnvType.CLIENT)
public class ChatComponent$State {
   private final List<GuiMessage> messages;
   private final List<String> history;
   private final List<ChatComponent$DelayedMessageDeletion> delayedMessageDeletions;

   public ChatComponent$State(
      final List<GuiMessage> messages, final List<String> history, final List<ChatComponent$DelayedMessageDeletion> delayedMessageDeletions
   ) {
      this.messages = messages;
      this.history = history;
      this.delayedMessageDeletions = delayedMessageDeletions;
   }
}
