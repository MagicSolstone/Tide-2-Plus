package net.minecraft.network.chat;

import net.minecraft.server.level.ServerPlayer;

public interface OutgoingChatMessage {
   Component content();

   void sendToPlayer(ServerPlayer player, boolean filtered, ChatType$Bound chatType);

   static OutgoingChatMessage create(final PlayerChatMessage message) {
      return (OutgoingChatMessage)(message.isSystem() ? new OutgoingChatMessage$Disguised(message.decoratedContent()) : new OutgoingChatMessage$Player(message));
   }
}
