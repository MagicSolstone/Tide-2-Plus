package net.minecraft.network.chat;

import net.minecraft.server.level.ServerPlayer;

public record OutgoingChatMessage$Player(PlayerChatMessage message) implements OutgoingChatMessage {
   @Override
   public Component content() {
      return this.message.decoratedContent();
   }

   @Override
   public void sendToPlayer(final ServerPlayer player, final boolean filtered, final ChatType$Bound chatType) {
      PlayerChatMessage filteredMessage = this.message.filter(filtered);
      if (!filteredMessage.isFullyFiltered()) {
         player.connection.sendPlayerChatMessage(filteredMessage, chatType);
      }
   }
}
