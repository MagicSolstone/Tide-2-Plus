package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket$Action;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class ClientSuggestionProvider$1 {
   static {
      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundCustomChatCompletionsPacket$Action[ClientboundCustomChatCompletionsPacket$Action.ADD
            .ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundCustomChatCompletionsPacket$Action[ClientboundCustomChatCompletionsPacket$Action.REMOVE
            .ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ClientboundCustomChatCompletionsPacket$Action[ClientboundCustomChatCompletionsPacket$Action.SET
            .ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
