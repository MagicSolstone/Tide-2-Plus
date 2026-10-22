package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

class ClientboundBossEventPacket$UpdatePropertiesOperation implements ClientboundBossEventPacket$Operation {
   private final boolean darkenScreen;
   private final boolean playMusic;
   private final boolean createWorldFog;

   private ClientboundBossEventPacket$UpdatePropertiesOperation(final boolean darkenScreen, final boolean playMusic, final boolean createWorldFog) {
      this.darkenScreen = darkenScreen;
      this.playMusic = playMusic;
      this.createWorldFog = createWorldFog;
   }

   private ClientboundBossEventPacket$UpdatePropertiesOperation(final RegistryFriendlyByteBuf input) {
      int flags = input.readUnsignedByte();
      this.darkenScreen = (flags & 1) > 0;
      this.playMusic = (flags & 2) > 0;
      this.createWorldFog = (flags & 4) > 0;
   }

   @Override
   public ClientboundBossEventPacket$OperationType getType() {
      return ClientboundBossEventPacket$OperationType.UPDATE_PROPERTIES;
   }

   @Override
   public void dispatch(final UUID id, final ClientboundBossEventPacket$Handler handler) {
      handler.updateProperties(id, this.darkenScreen, this.playMusic, this.createWorldFog);
   }

   @Override
   public void write(final RegistryFriendlyByteBuf output) {
      output.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
   }
}
