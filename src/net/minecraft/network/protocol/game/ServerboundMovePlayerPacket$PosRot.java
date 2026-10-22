package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.world.phys.Vec3;

public class ServerboundMovePlayerPacket$PosRot extends ServerboundMovePlayerPacket {
   public static final StreamCodec<FriendlyByteBuf, ServerboundMovePlayerPacket$PosRot> STREAM_CODEC = Packet.codec(
      ServerboundMovePlayerPacket$PosRot::write, ServerboundMovePlayerPacket$PosRot::read
   );

   public ServerboundMovePlayerPacket$PosRot(final Vec3 pos, final float yRot, final float xRot, final boolean onGround, final boolean horizontalCollision) {
      super(pos.x, pos.y, pos.z, yRot, xRot, onGround, horizontalCollision, true, true);
   }

   public ServerboundMovePlayerPacket$PosRot(
      final double x, final double y, final double z, final float yRot, final float xRot, final boolean onGround, final boolean horizontalCollision
   ) {
      super(x, y, z, yRot, xRot, onGround, horizontalCollision, true, true);
   }

   private static ServerboundMovePlayerPacket$PosRot read(final FriendlyByteBuf input) {
      double x = input.readDouble();
      double y = input.readDouble();
      double z = input.readDouble();
      float yRot = input.readFloat();
      float xRot = input.readFloat();
      short flags = input.readUnsignedByte();
      boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
      boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
      return new ServerboundMovePlayerPacket$PosRot(x, y, z, yRot, xRot, onGround, horizontalCollision);
   }

   private void write(final FriendlyByteBuf output) {
      output.writeDouble(this.x);
      output.writeDouble(this.y);
      output.writeDouble(this.z);
      output.writeFloat(this.yRot);
      output.writeFloat(this.xRot);
      output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
   }

   @Override
   public PacketType<ServerboundMovePlayerPacket$PosRot> type() {
      return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS_ROT;
   }
}
