package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

interface ClientboundBossEventPacket$Operation {
   ClientboundBossEventPacket$OperationType getType();

   void dispatch(UUID id, ClientboundBossEventPacket$Handler handler);

   void write(RegistryFriendlyByteBuf output);
}
