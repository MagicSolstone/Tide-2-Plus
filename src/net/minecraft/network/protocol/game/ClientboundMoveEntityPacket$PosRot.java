package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public class ClientboundMoveEntityPacket$PosRot extends ClientboundMoveEntityPacket {
   public static final StreamCodec<FriendlyByteBuf, ClientboundMoveEntityPacket$PosRot> STREAM_CODEC = Packet.codec(
      ClientboundMoveEntityPacket$PosRot::write, ClientboundMoveEntityPacket$PosRot::read
   );

   public ClientboundMoveEntityPacket$PosRot(final int id, final VecDelta delta, final byte yRot, final byte xRot, final boolean onGround) {
      super(id, delta, yRot, xRot, onGround, true, true);
   }

   private static ClientboundMoveEntityPacket$PosRot read(final FriendlyByteBuf input) {
      int entityId = input.readVarInt();
      int properties = input.readVarInt();
      VecDelta delta = VecDelta.read(input, unpackStepCount(properties));
      byte yRot = input.readByte();
      byte xRot = input.readByte();
      return new ClientboundMoveEntityPacket$PosRot(entityId, delta, yRot, xRot, unpackOnGround(properties));
   }

   private void write(final FriendlyByteBuf output) {
      output.writeVarInt(this.entityId);
      output.writeVarInt(packProperties(this.onGround, this.delta.stepCount()));
      VecDelta.write(output, this.delta);
      output.writeByte(this.yRot);
      output.writeByte(this.xRot);
   }

   @Override
   public PacketType<ClientboundMoveEntityPacket$PosRot> type() {
      return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS_ROT;
   }
}
