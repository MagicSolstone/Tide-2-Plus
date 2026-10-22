package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

record ClientboundBossEventPacket$UpdateNameOperation(Component name) implements ClientboundBossEventPacket$Operation {
   private ClientboundBossEventPacket$UpdateNameOperation(final RegistryFriendlyByteBuf input) {
      this(ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input));
   }

   @Override
   public ClientboundBossEventPacket$OperationType getType() {
      return ClientboundBossEventPacket$OperationType.UPDATE_NAME;
   }

   @Override
   public void dispatch(final UUID id, final ClientboundBossEventPacket$Handler handler) {
      handler.updateName(id, this.name);
   }

   @Override
   public void write(final RegistryFriendlyByteBuf output) {
      ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.name);
   }
}
