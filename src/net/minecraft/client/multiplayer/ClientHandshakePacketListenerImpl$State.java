package net.minecraft.client.multiplayer;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
enum ClientHandshakePacketListenerImpl$State {
   CONNECTING(Component.translatable("connect.connecting"), Set.of()),
   AUTHORIZING(Component.translatable("connect.authorizing"), Set.of(CONNECTING)),
   ENCRYPTING(Component.translatable("connect.encrypting"), Set.of(AUTHORIZING)),
   JOINING(Component.translatable("connect.joining"), Set.of(ENCRYPTING, CONNECTING));

   private final Component message;
   private final Set<ClientHandshakePacketListenerImpl$State> fromStates;

   private ClientHandshakePacketListenerImpl$State(final Component message, final Set<ClientHandshakePacketListenerImpl$State> fromStates) {
      this.message = message;
      this.fromStates = fromStates;
   }
}
