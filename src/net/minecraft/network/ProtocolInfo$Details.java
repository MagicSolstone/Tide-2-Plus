package net.minecraft.network;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.util.VisibleForDebug;

public interface ProtocolInfo$Details {
   ConnectionProtocol id();

   PacketFlow flow();

   @VisibleForDebug
   void listPackets(ProtocolInfo$Details$PacketVisitor output);
}
