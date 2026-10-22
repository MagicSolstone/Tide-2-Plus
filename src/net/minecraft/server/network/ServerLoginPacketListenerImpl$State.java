package net.minecraft.server.network;

enum ServerLoginPacketListenerImpl$State {
   HELLO,
   KEY,
   AUTHENTICATING,
   NEGOTIATING,
   VERIFYING,
   WAITING_FOR_DUPE_DISCONNECT,
   PROTOCOL_SWITCHING,
   ACCEPTED;
}
