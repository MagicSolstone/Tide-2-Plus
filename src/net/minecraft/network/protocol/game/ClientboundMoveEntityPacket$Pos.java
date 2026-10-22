package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public class ClientboundMoveEntityPacket$Pos extends ClientboundMoveEntityPacket {
   public static final StreamCodec<FriendlyByteBuf, ClientboundMoveEntityPacket$Pos> STREAM_CODEC = Packet.codec(
      ClientboundMoveEntityPacket$Pos::write, ClientboundMoveEntityPacket$Pos::read
   );

   public ClientboundMoveEntityPacket$Pos(final int id, final VecDelta delta, final boolean onGround) {
      super(id, delta, (byte)0, (byte)0, onGround, false, true);
   }

   private static ClientboundMoveEntityPacket$Pos read(final FriendlyByteBuf input) {
      int entityId = input.readVarInt();
      int properties = input.readVarInt();
      VecDelta delta = VecDelta.read(input, unpackStepCount(properties));
      return new ClientboundMoveEntityPacket$Pos(entityId, delta, unpackOnGround(properties));
   }

   private void write(final FriendlyByteBuf output) {
      output.writeVarInt(this.entityId);
      output.writeVarInt(packProperties(this.onGround, this.delta.stepCount()));
      VecDelta.write(output, this.delta);
   }

   @Override
   public PacketType<ClientboundMoveEntityPacket$Pos> type() {
      return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS;
   }
}
