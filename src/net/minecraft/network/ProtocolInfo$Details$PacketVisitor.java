package net.minecraft.network;

import net.minecraft.network.protocol.PacketType;

@FunctionalInterface
public interface ProtocolInfo$Details$PacketVisitor {
   void accept(PacketType<?> type, int networkId);
}
