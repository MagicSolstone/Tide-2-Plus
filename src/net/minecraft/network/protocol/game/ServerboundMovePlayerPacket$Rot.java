package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public class ServerboundMovePlayerPacket$Rot extends ServerboundMovePlayerPacket {
   public static final StreamCodec<FriendlyByteBuf, ServerboundMovePlayerPacket$Rot> STREAM_CODEC = Packet.codec(
      ServerboundMovePlayerPacket$Rot::write, ServerboundMovePlayerPacket$Rot::read
   );

   public ServerboundMovePlayerPacket$Rot(final float yRot, final float xRot, final boolean onGround, final boolean horizontalCollision) {
      super(0.0, 0.0, 0.0, yRot, xRot, onGround, horizontalCollision, false, true);
   }

   private static ServerboundMovePlayerPacket$Rot read(final FriendlyByteBuf input) {
      float yRot = input.readFloat();
      float xRot = input.readFloat();
      short flags = input.readUnsignedByte();
      boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
      boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
      return new ServerboundMovePlayerPacket$Rot(yRot, xRot, onGround, horizontalCollision);
   }

   private void write(final FriendlyByteBuf output) {
      output.writeFloat(this.yRot);
      output.writeFloat(this.xRot);
      output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
   }

   @Override
   public PacketType<ServerboundMovePlayerPacket$Rot> type() {
      return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_ROT;
   }
}
