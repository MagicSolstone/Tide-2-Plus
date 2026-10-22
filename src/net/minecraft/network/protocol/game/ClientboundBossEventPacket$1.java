package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

class ClientboundBossEventPacket$1 implements ClientboundBossEventPacket$Operation {
   @Override
   public ClientboundBossEventPacket$OperationType getType() {
      return ClientboundBossEventPacket$OperationType.REMOVE;
   }

   @Override
   public void dispatch(final UUID id, final ClientboundBossEventPacket$Handler handler) {
      handler.remove(id);
   }

   @Override
   public void write(final RegistryFriendlyByteBuf output) {
   }
}
