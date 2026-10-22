package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public class ClientboundMoveEntityPacket$Rot extends ClientboundMoveEntityPacket {
   public static final StreamCodec<FriendlyByteBuf, ClientboundMoveEntityPacket$Rot> STREAM_CODEC = Packet.codec(
      ClientboundMoveEntityPacket$Rot::write, ClientboundMoveEntityPacket$Rot::read
   );

   public ClientboundMoveEntityPacket$Rot(final int id, final byte yRot, final byte xRot, final boolean onGround) {
      super(id, VecDelta.ZERO, yRot, xRot, onGround, true, false);
   }

   private static ClientboundMoveEntityPacket$Rot read(final FriendlyByteBuf input) {
      int entityId = input.readVarInt();
      boolean onGround = input.readBoolean();
      byte yRot = input.readByte();
      byte xRot = input.readByte();
      return new ClientboundMoveEntityPacket$Rot(entityId, yRot, xRot, onGround);
   }

   private void write(final FriendlyByteBuf output) {
      output.writeVarInt(this.entityId);
      output.writeBoolean(this.onGround);
      output.writeByte(this.yRot);
      output.writeByte(this.xRot);
   }

   @Override
   public PacketType<ClientboundMoveEntityPacket$Rot> type() {
      return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_ROT;
   }
}
