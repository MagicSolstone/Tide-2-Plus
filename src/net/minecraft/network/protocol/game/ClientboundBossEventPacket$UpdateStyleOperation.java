package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.BossEvent$BossBarColor;
import net.minecraft.world.BossEvent$BossBarOverlay;

class ClientboundBossEventPacket$UpdateStyleOperation implements ClientboundBossEventPacket$Operation {
   private final BossEvent$BossBarColor color;
   private final BossEvent$BossBarOverlay overlay;

   private ClientboundBossEventPacket$UpdateStyleOperation(final BossEvent$BossBarColor color, final BossEvent$BossBarOverlay overlay) {
      this.color = color;
      this.overlay = overlay;
   }

   private ClientboundBossEventPacket$UpdateStyleOperation(final RegistryFriendlyByteBuf input) {
      this.color = input.readEnum(BossEvent$BossBarColor.class);
      this.overlay = input.readEnum(BossEvent$BossBarOverlay.class);
   }

   @Override
   public ClientboundBossEventPacket$OperationType getType() {
      return ClientboundBossEventPacket$OperationType.UPDATE_STYLE;
   }

   @Override
   public void dispatch(final UUID id, final ClientboundBossEventPacket$Handler handler) {
      handler.updateStyle(id, this.color, this.overlay);
   }

   @Override
   public void write(final RegistryFriendlyByteBuf output) {
      output.writeEnum(this.color);
      output.writeEnum(this.overlay);
   }
}
