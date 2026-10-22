package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.BossEvent;
import net.minecraft.world.BossEvent$BossBarColor;
import net.minecraft.world.BossEvent$BossBarOverlay;

class ClientboundBossEventPacket$AddOperation implements ClientboundBossEventPacket$Operation {
   private final Component name;
   private final float progress;
   private final BossEvent$BossBarColor color;
   private final BossEvent$BossBarOverlay overlay;
   private final boolean darkenScreen;
   private final boolean playMusic;
   private final boolean createWorldFog;

   private ClientboundBossEventPacket$AddOperation(final BossEvent event) {
      this.name = event.getName();
      this.progress = event.getProgress();
      this.color = event.getColor();
      this.overlay = event.getOverlay();
      this.darkenScreen = event.shouldDarkenScreen();
      this.playMusic = event.shouldPlayBossMusic();
      this.createWorldFog = event.shouldCreateWorldFog();
   }

   private ClientboundBossEventPacket$AddOperation(final RegistryFriendlyByteBuf input) {
      this.name = ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
      this.progress = input.readFloat();
      this.color = input.readEnum(BossEvent$BossBarColor.class);
      this.overlay = input.readEnum(BossEvent$BossBarOverlay.class);
      int flags = input.readUnsignedByte();
      this.darkenScreen = (flags & 1) > 0;
      this.playMusic = (flags & 2) > 0;
      this.createWorldFog = (flags & 4) > 0;
   }

   @Override
   public ClientboundBossEventPacket$OperationType getType() {
      return ClientboundBossEventPacket$OperationType.ADD;
   }

   @Override
   public void dispatch(final UUID id, final ClientboundBossEventPacket$Handler handler) {
      handler.add(id, this.name, this.progress, this.color, this.overlay, this.darkenScreen, this.playMusic, this.createWorldFog);
   }

   @Override
   public void write(final RegistryFriendlyByteBuf output) {
      ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.name);
      output.writeFloat(this.progress);
      output.writeEnum(this.color);
      output.writeEnum(this.overlay);
      output.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
   }
}
