package net.minecraft.server.network;

import net.minecraft.network.protocol.handshake.ClientIntent;

// $VF: synthetic class
class ServerHandshakePacketListenerImpl$1 {
   static {
      try {
         $SwitchMap$net$minecraft$network$protocol$handshake$ClientIntent[ClientIntent.LOGIN.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$handshake$ClientIntent[ClientIntent.STATUS.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$handshake$ClientIntent[ClientIntent.TRANSFER.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
