package net.minecraft.network.chat;

import net.minecraft.server.level.ServerPlayer;

public record OutgoingChatMessage$Disguised(Component content) implements OutgoingChatMessage {
   @Override
   public void sendToPlayer(final ServerPlayer player, final boolean filtered, final ChatType$Bound chatType) {
      player.connection.sendDisguisedChatMessage(this.content, chatType);
   }
}
