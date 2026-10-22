package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

record ClientboundBossEventPacket$UpdateProgressOperation(float progress) implements ClientboundBossEventPacket$Operation {
   private ClientboundBossEventPacket$UpdateProgressOperation(final RegistryFriendlyByteBuf input) {
      this(input.readFloat());
   }

   @Override
   public ClientboundBossEventPacket$OperationType getType() {
      return ClientboundBossEventPacket$OperationType.UPDATE_PROGRESS;
   }

   @Override
   public void dispatch(final UUID id, final ClientboundBossEventPacket$Handler handler) {
      handler.updateProgress(id, this.progress);
   }

   @Override
   public void write(final RegistryFriendlyByteBuf output) {
      output.writeFloat(this.progress);
   }
}
