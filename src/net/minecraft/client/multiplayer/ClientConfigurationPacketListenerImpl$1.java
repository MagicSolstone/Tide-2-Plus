package net.minecraft.client.multiplayer;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.protocol.game.GameProtocols$Context;

@Environment(EnvType.CLIENT)
class ClientConfigurationPacketListenerImpl$1 implements GameProtocols$Context {
   ClientConfigurationPacketListenerImpl$1(final ClientConfigurationPacketListenerImpl this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public boolean hasInfiniteMaterials() {
      return true;
   }

   @Override
   public boolean canUseCommandBlocks() {
      return true;
   }
}
