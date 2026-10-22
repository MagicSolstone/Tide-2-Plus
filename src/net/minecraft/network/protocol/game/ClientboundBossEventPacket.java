package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.world.BossEvent;

public class ClientboundBossEventPacket implements Packet<ClientGamePacketListener> {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBossEventPacket> STREAM_CODEC = Packet.codec(
      ClientboundBossEventPacket::write, ClientboundBossEventPacket::new
   );
   private static final int FLAG_DARKEN = 1;
   private static final int FLAG_MUSIC = 2;
   private static final int FLAG_FOG = 4;
   private final UUID id;
   private final ClientboundBossEventPacket$Operation operation;
   private static final ClientboundBossEventPacket$Operation REMOVE_OPERATION = new ClientboundBossEventPacket$1();

   private ClientboundBossEventPacket(final UUID id, final ClientboundBossEventPacket$Operation operation) {
      this.id = id;
      this.operation = operation;
   }

   private ClientboundBossEventPacket(final RegistryFriendlyByteBuf input) {
      this.id = input.readUUID();
      ClientboundBossEventPacket$OperationType type = input.readEnum(ClientboundBossEventPacket$OperationType.class);
      this.operation = type.reader.decode(input);
   }

   public static ClientboundBossEventPacket createAddPacket(final BossEvent event) {
      return new ClientboundBossEventPacket(event.getId(), new ClientboundBossEventPacket$AddOperation(event));
   }

   public static ClientboundBossEventPacket createRemovePacket(final UUID id) {
      return new ClientboundBossEventPacket(id, REMOVE_OPERATION);
   }

   public static ClientboundBossEventPacket createUpdateProgressPacket(final BossEvent event) {
      return new ClientboundBossEventPacket(event.getId(), new ClientboundBossEventPacket$UpdateProgressOperation(event.getProgress()));
   }

   public static ClientboundBossEventPacket createUpdateNamePacket(final BossEvent event) {
      return new ClientboundBossEventPacket(event.getId(), new ClientboundBossEventPacket$UpdateNameOperation(event.getName()));
   }

   public static ClientboundBossEventPacket createUpdateStylePacket(final BossEvent event) {
      return new ClientboundBossEventPacket(event.getId(), new ClientboundBossEventPacket$UpdateStyleOperation(event.getColor(), event.getOverlay()));
   }

   public static ClientboundBossEventPacket createUpdatePropertiesPacket(final BossEvent event) {
      return new ClientboundBossEventPacket(
         event.getId(),
         new ClientboundBossEventPacket$UpdatePropertiesOperation(event.shouldDarkenScreen(), event.shouldPlayBossMusic(), event.shouldCreateWorldFog())
      );
   }

   private void write(final RegistryFriendlyByteBuf output) {
      output.writeUUID(this.id);
      output.writeEnum(this.operation.getType());
      this.operation.write(output);
   }

   private static int encodeProperties(final boolean darkenScreen, final boolean playMusic, final boolean createWorldFog) {
      int properties = 0;
      if (darkenScreen) {
         properties |= 1;
      }

      if (playMusic) {
         properties |= 2;
      }

      if (createWorldFog) {
         properties |= 4;
      }

      return properties;
   }

   @Override
   public PacketType<ClientboundBossEventPacket> type() {
      return GamePacketTypes.CLIENTBOUND_BOSS_EVENT;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleBossUpdate(this);
   }

   public void dispatch(final ClientboundBossEventPacket$Handler handler) {
      this.operation.dispatch(this.id, handler);
   }
}
