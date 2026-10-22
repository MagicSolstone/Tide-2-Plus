package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamDecoder;

enum ClientboundBossEventPacket$OperationType {
   ADD(ClientboundBossEventPacket$AddOperation::new),
   REMOVE(input -> ClientboundBossEventPacket.REMOVE_OPERATION),
   UPDATE_PROGRESS(ClientboundBossEventPacket$UpdateProgressOperation::new),
   UPDATE_NAME(ClientboundBossEventPacket$UpdateNameOperation::new),
   UPDATE_STYLE(ClientboundBossEventPacket$UpdateStyleOperation::new),
   UPDATE_PROPERTIES(ClientboundBossEventPacket$UpdatePropertiesOperation::new);

   private final StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket$Operation> reader;

   private ClientboundBossEventPacket$OperationType(final StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket$Operation> reader) {
      this.reader = reader;
   }
}
